package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.computable.operation.*
import com.tobi.mc.intermediate.construct.TacExpression

data class TacMathOperation(val arg1: TacVariableReference, val type: MathType, val arg2: TacVariableReference) : TacExpression {

    enum class MathType(val opString: String) {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MOD("%"),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        EQUALS("=="),
        NOT_EQUALS("!=")
    }

    override fun toString(): String {
        return "$arg1 ${type.opString} $arg2"
    }

    companion object {

        fun getType(operation: MathOperation): MathType = when(operation) {
            is Add -> MathType.ADD
            is Subtract -> MathType.SUBTRACT
            is Multiply -> MathType.MULTIPLY
            is Divide -> MathType.DIVIDE
            is Mod -> MathType.MOD
            is GreaterThan -> MathType.GREATER_THAN
            is GreaterThanOrEqualTo -> MathType.GREATER_THAN_OR_EQUAL
            is LessThan -> MathType.LESS_THAN
            is LessThanOrEqualTo -> MathType.LESS_THAN_OR_EQUAL
            is Equals -> MathType.EQUALS
            is NotEquals -> MathType.NOT_EQUALS
            else -> throw IllegalArgumentException("Unknown operation ${operation::class.simpleName}")
        }
    }
}