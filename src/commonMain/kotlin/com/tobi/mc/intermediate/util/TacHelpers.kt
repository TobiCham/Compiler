package com.tobi.mc.intermediate.util

import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

fun TacStructure.getComponents(): Array<TacStructure> = when(this) {
    is ConstructBranchEqualZero -> arrayOf(this.conditionVariable)
    is ConstructSetVariable -> arrayOf(this.variable, this.value)
    is ConstructPushArgument -> arrayOf(this.variable)
    is ConstructPopArgument -> emptyArray()
    is ConstructLabel -> emptyArray()
    is ConstructGoto -> emptyArray()
    is ConstructReturn -> emptyArray()
    is ConstructFunctionCall -> arrayOf(this.function)
    is ConstructStringConcat -> arrayOf(this.str1, this.str2)
    is ConstructUnaryMinus -> arrayOf(this.variable)
    is ConstructNegation -> arrayOf(this.toNegate)
    is ConstructMath -> arrayOf(this.arg1, this.arg2)
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