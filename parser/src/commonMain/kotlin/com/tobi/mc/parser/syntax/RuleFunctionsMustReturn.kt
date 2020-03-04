package com.tobi.mc.parser.syntax

import com.tobi.mc.ParseException
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.util.DescriptionMeta

internal object RuleFunctionsMustReturn : SimpleRule {

    override val description: DescriptionMeta = SimpleDescription("Functions must return", """
        With the exception of void functions, ensures that there is a return expression on all possible paths
    """.trimIndent())

    override fun validate(computable: Computable, state: Unit) {
        if(computable !is FunctionDeclaration) return
        if(computable.returnType != DataType.VOID && computable.returnType != null && !computable.body.hasReturnPath()) {
            throw ParseException("Function '${computable.name}' must have a return on all possible paths")
        }
    }

    private fun Computable.hasReturnPath(): Boolean {
        return when(this) {
            is ReturnExpression -> true
            is IfStatement -> elseBody != null && this.ifBody.hasReturnPath() && elseBody!!.hasReturnPath()
            is ExpressionSequence -> operations.any { it.hasReturnPath() }
            else -> false
        }
    }
}