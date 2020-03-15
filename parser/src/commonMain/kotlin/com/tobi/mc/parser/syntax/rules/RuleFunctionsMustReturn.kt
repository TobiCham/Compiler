package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object RuleFunctionsMustReturn : InstanceSyntaxRule<FunctionDeclaration>(FunctionDeclaration::class) {

    override val description: DescriptionMeta = SimpleDescription("Functions must return", """
        With the exception of void or auto functions, ensures that there is a return expression on all possible paths
    """.trimIndent())

    override fun FunctionDeclaration.validate() {
        if(returnType != DataType.VOID && returnType != null && !body.hasReturnPath()) {
            throw ParseException("Function '${name}' must have a return on all possible paths")
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