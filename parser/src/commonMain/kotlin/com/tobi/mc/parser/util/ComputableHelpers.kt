package com.tobi.mc.parser.util

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.copyAndReplaceIndex
import com.tobi.mc.util.typeName
import kotlin.reflect.KMutableProperty0

@Suppress("UNCHECKED_CAST")
fun <T : Computable> T.clone(): T = when(this) {
    is Data -> this
    is ContinueStatement, BreakStatement -> this
    is ReturnExpression -> ReturnExpression(toReturn?.clone())
    is IfStatement -> IfStatement(check.clone(), ifBody.clone(), elseBody?.clone())
    is WhileLoop -> WhileLoop(check.clone(), body.clone())
    is ExpressionSequence -> ExpressionSequence(operations.map(Computable::clone).toMutableList())
    is GetVariable -> GetVariable(name)
    is SetVariable -> SetVariable(name, value.clone())
    is DefineVariable -> DefineVariable(name, value.clone(), expectedType)
    is FunctionDeclaration -> FunctionDeclaration(name, ArrayList(parameters), body.clone(), returnType)
    is FunctionCall -> FunctionCall(function.clone(), arguments.map(DataComputable::clone).toMutableList())
    is Add -> Add(arg1.clone(), arg2.clone())
    is Subtract -> Subtract(arg1.clone(), arg2.clone())
    is Multiply -> Multiply(arg1.clone(), arg2.clone())
    is Divide -> Divide(arg1.clone(), arg2.clone())
    is Mod -> Mod(arg1.clone(), arg2.clone())
    is GreaterThan -> GreaterThan(arg1.clone(), arg2.clone())
    is LessThan -> LessThan(arg1.clone(), arg2.clone())
    is GreaterThanOrEqualTo -> GreaterThanOrEqualTo(arg1.clone(), arg2.clone())
    is LessThanOrEqualTo -> LessThanOrEqualTo(arg1.clone(), arg2.clone())
    is Equals -> Equals(arg1.clone(), arg2.clone())
    is NotEquals -> NotEquals(arg1.clone(), arg2.clone())
    is Negation -> Negation(negation.clone())
    else -> throw IllegalStateException("Invalid value ${this.typeName}")
} as T

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
        is ReturnExpression -> update(this::toReturn)
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
                if(index == 0) this.function = component as DataComputable
                else this.arguments = this.arguments.copyAndReplaceIndex(index - 1, component) as List<DataComputable>
                true
            } else false
        }
        is MathOperation -> update(this::arg1, this::arg2)
        is Negation -> update(this::negation)
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
    is ContinueStatement, BreakStatement -> emptyArray()
    is Data -> emptyArray()
    is ReturnExpression -> if(toReturn == null) emptyArray() else arrayOf(toReturn!!)
    is IfStatement -> if(elseBody == null) arrayOf(check, ifBody) else arrayOf(check, ifBody, elseBody!!)
    is WhileLoop -> arrayOf(check, body)
    is ExpressionSequence -> operations.toTypedArray()
    is GetVariable -> emptyArray()
    is SetVariable -> arrayOf(value)
    is DefineVariable -> arrayOf(value)
    is FunctionDeclaration -> arrayOf(body)
    is FunctionCall -> arrayOf(function, *arguments.toTypedArray())
    is MathOperation -> arrayOf(arg1, arg2)
    is Negation -> arrayOf(negation)
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