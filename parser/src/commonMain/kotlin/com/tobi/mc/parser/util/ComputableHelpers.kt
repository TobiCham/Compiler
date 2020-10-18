package com.tobi.mc.parser.util

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.copyAndReplaceIndex
import com.tobi.mc.util.typeName
import kotlin.reflect.KMutableProperty0

@Suppress("UNCHECKED_CAST")
fun Computable.updateComponentAtIndex(index: Int, component: Computable) {
    fun update(vararg properties: KMutableProperty0<out Computable?>): Boolean {
        (properties[index] as KMutableProperty0<Computable>).set(component)
        if(index < 0 || index >= properties.size) {
            return false
        }
        return true
    }
    val updated = when(this) {
        is ReturnStatement -> update(this::toReturn)
        is IfStatement -> update(this::check, this::ifBody, this::elseBody)
        is WhileLoop -> update(this::check, this::body)
        is ExpressionSequence -> {
            if(index >= 0 && index < this.operations.size) {
                this.operations = newList(this.operations, index, component)
                true
            } else false
        }
        is SetVariable -> update(this::value)
        is DefineVariable -> update(this::value)
        is FunctionDeclaration -> update(this::body)
        is FunctionCall -> {
            if(index >= 0 && index < this.arguments.size + 1) {
                if(index == 0) this.function = component
                else this.arguments = this.arguments.copyAndReplaceIndex(index - 1, component)
                true
            } else false
        }
        is MathOperation -> update(this::arg1, this::arg2)
        is UnaryMinus -> update(this::expression)
        is Negation -> update(this::negation)
        is StringConcat -> update(this::str1, this::str2)
        is Program -> update(this::code)
        else -> throw IllegalStateException("Can't update components for ${this.typeName}")
    }
    if(!updated) {
        throw IllegalStateException("Invalid component index $index for ${this.typeName}")
    }
}

private fun newList(list: List<Computable>, index: Int, newValue: Computable) = list.mapIndexed { i, computable ->
    if(i == index) newValue else computable
}

fun Computable.getComponents(): Array<Computable> = when(this) {
    is ContinueStatement -> emptyArray()
    is BreakStatement -> emptyArray()
    is Data -> emptyArray()
    is ReturnStatement -> if(toReturn == null) emptyArray() else arrayOf(toReturn!!)
    is IfStatement -> if(elseBody == null) arrayOf(check, ifBody) else arrayOf(check, ifBody, elseBody!!)
    is WhileLoop -> arrayOf(check, body)
    is ExpressionSequence -> operations.toTypedArray()
    is GetVariable -> emptyArray()
    is SetVariable -> arrayOf(value)
    is DefineVariable -> arrayOf(value)
    is FunctionDeclaration -> arrayOf(body)
    is FunctionCall -> arrayOf(function, *arguments)
    is MathOperation -> arrayOf(arg1, arg2)
    is UnaryMinus -> arrayOf(expression)
    is Negation -> arrayOf(negation)
    is StringConcat -> arrayOf(str1, str2)
    is Program -> arrayOf(code)
    else -> throw IllegalStateException("Can't get components for ${this.typeName}")
}

fun Computable.traverseAllNodes(): Sequence<Computable> = sequence {
    yield(this@traverseAllNodes)

    val stack = ArrayListStack<Computable>()
    stack.push(this@traverseAllNodes)

    while(!stack.isEmpty()) {
        val computable = stack.pop()
        yield(computable)

        val components = computable.getComponents()
        for(i in components.size - 1 downTo 0) {
            stack.push(components[i])
        }
    }
}


internal fun Computable.isNumber(number: Long) = this is DataTypeInt && this.value == number
internal fun Computable.isZero() = this.isNumber(0)
internal fun Computable.isOne() = this.isNumber(1)

internal fun <T : Computable> T.copySource(start: Computable, end: Computable): T {
    if(start.sourceRange != null && end.sourceRange != null) {
        this.sourceRange = SourceRange(start.sourceRange!!.start, end.sourceRange!!.end)
    }
    return this
}