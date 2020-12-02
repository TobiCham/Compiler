package com.tobi.mc.intermediate.util

import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

fun TacStructure.getComponents(): Array<TacStructure> = when(this) {
    is TacBranchEqualZero -> arrayOf(this.conditionVariable)
    is TacSetVariable -> arrayOf(this.variable, this.value)
    is TacSetArgument -> arrayOf(this.variable)
    is TacLabel -> emptyArray()
    is TacGoto -> emptyArray()
    is TacReturn -> emptyArray()
    is TacFunctionCall -> arrayOf(this.function)
    is TacStringConcat -> arrayOf(this.str1, this.str2)
    is TacUnaryMinus -> arrayOf(this.variable)
    is TacNegation -> arrayOf(this.toNegate)
    is TacMathOperation -> arrayOf(this.arg1, this.arg2)
    is TacFunction -> arrayOf(*this.code.toTypedArray())
    is TacProgram -> arrayOf(this.code)
    is TacVariableReference -> emptyArray()
    is TacInbuiltFunction -> emptyArray()
    else -> throw IllegalStateException()
}

fun TacStructure.asDeepSequence(filter: (structure: TacStructure) -> Boolean = { true }) = sequence {
    val stack: MutableStack<TacStructure> = ArrayListStack()
    stack.push(this@asDeepSequence)

    while(!stack.isEmpty()) {
        val element = stack.pop()
        if(filter(element)) {
            yield(element)
            element.getComponents().forEach(stack::push)
        }
    }
}

fun List<TacStructure>.asDeepSequence(filter: (structure: TacStructure) -> Boolean = { true }) = sequence {
    for(item in this@asDeepSequence) {
        for(structure in item.asDeepSequence(filter)) {
            yield(structure)
        }
    }
}