package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.parser.ParseException

object RuleFunctionsMustReturn : SimpleRule {

    override fun validate(computable: Computable, state: Unit) {
        if(computable !is FunctionDeclaration) return
        if(computable.returnType != DataType.VOID && !computable.body.hasReturnPath()) {
            throw ParseException("Function '${computable.name}' must have a return on all possible paths")
        }
    }

    private fun Computable.hasReturnPath(): Boolean {
        return when(this) {
            is ReturnExpression -> true
            is IfStatement -> elseBody != null && this.ifBody.hasReturnPath() && elseBody.hasReturnPath()
            is ExpressionSequence -> operations.any { it.hasReturnPath() }
            else -> false
        }
    }
}