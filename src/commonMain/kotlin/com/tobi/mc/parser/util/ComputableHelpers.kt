package com.tobi.mc.parser.util

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.copyAndReplaceIndex
import com.tobi.mc.util.typeName
import kotlin.reflect.KMutableProperty0

@Suppress("UNCHECKED_CAST")
fun Computable.updateNodeAtIndex(index: Int, node: Computable?) {
    fun update(vararg properties: KMutableProperty0<out Computable?>): Boolean {
        (properties[index] as KMutableProperty0<Computable?>).set(node)
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
            val functions = this.expressions.count { it is FunctionDeclaration }
            if(index < functions || index < 0 || index >= this.expressions.size + functions) {
                false
            } else {
                if(node == null) {
                    this.expressions.removeAt(index - functions)
                } else {
                    this.expressions.set(index - functions, node)
                }
                true
            }
        }
        is SetVariable -> update(this::value)
        is VariableDeclaration -> update(this::value)
        is FunctionDeclaration -> update(this::body)
        is FunctionCall -> {
            if(node == null) {
                throw IllegalArgumentException("Node can't be null")
            }
            if(index >= 0 && index < this.arguments.size + 1) {
                if(index == 0) this.function = node
                else this.arguments = this.arguments.copyAndReplaceIndex(index - 1, node)
                true
            } else false
        }
        is MathOperation -> update(this::arg1, this::arg2)
        is UnaryMinus -> update(this::expression)
        is Negation -> update(this::negation)
        is StringConcat -> update(this::str1, this::str2)
        is Program -> update(this::code)
        else -> throw IllegalStateException("Can't update nodes for ${this.typeName}")
    }
    if(!updated) {
        throw IllegalStateException("Invalid node index $index for ${this.typeName}")
    }
}

fun Computable.traverseAllNodes(): Sequence<Computable> = sequence {
    val stack = ArrayListStack<Computable>()
    stack.push(this@traverseAllNodes)

    while(!stack.isEmpty()) {
        val computable = stack.pop()
        yield(computable)

        computable.getNodes().reversed().forEach(stack::push)
    }
}

fun Computable.traverseWithDepth(depth: Int = 0): Sequence<Pair<Computable, Int>> = sequence {
    yield(Pair(this@traverseWithDepth, depth))

    val newDepth = when(this@traverseWithDepth) {
        is ExpressionSequence -> depth + 1
        is FunctionDeclaration -> depth + 1
        else -> depth
    }
    for(node in this@traverseWithDepth.getNodes()) {
        yieldAll(node.traverseWithDepth(newDepth))
    }
}

fun Computable.isNumber(number: Long) = this is DataTypeInt && this.value == number
fun Computable.isZero() = this.isNumber(0)
fun Computable.isOne() = this.isNumber(1)

fun <T : Computable> T.copySource(start: Computable, end: Computable): T {
    if(start.sourceRange != null && end.sourceRange != null) {
        this.sourceRange = SourceRange(start.sourceRange!!.start, end.sourceRange!!.end)
    }
    return this
}