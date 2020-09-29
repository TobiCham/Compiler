package com.tobi.mc.intermediate.util

import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.*

fun TacStructure.getComponents(): Array<TacStructure> = when(this) {
    is TacProgram -> arrayOf(*this.environments.toTypedArray(), *this.functions.toTypedArray())
    is TacEnvironment -> emptyArray()
    is TacFunction -> arrayOf(this.environment, *this.code.toTypedArray())
    is ConstructNegation -> arrayOf(this.toNegate)
    is ConstructFunctionCall -> arrayOf(this.function)
    is TacVariableReference -> emptyArray()
    is ConstructMath -> arrayOf(this.arg1, this.arg2)
    is ConstructStringConcat -> arrayOf(this.str1, this.str2)
    is ConstructUnaryMinus -> arrayOf(this.variable)
    is ConstructLabel -> emptyArray()
    is ConstructBranchZero -> arrayOf(this.conditionVariable)
    is ConstructPushArgument -> arrayOf(this.variable)
    is ConstructGoto -> emptyArray()
    is ConstructCreateClosure -> arrayOf(this.environment)
    is ConstructPopArgument -> emptyArray()
    is ConstructReturn -> if(this.toReturn == null) emptyArray() else arrayOf(this.toReturn as TacStructure)
    is ConstructSetVariable -> arrayOf(this.variable, this.value)
    else -> throw IllegalStateException()
}