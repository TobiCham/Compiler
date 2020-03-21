package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
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
            is IfStatement -> this.ifBody.hasReturnPath() && !(elseBody != null && !elseBody!!.hasReturnPath())
            is ExpressionSequence -> operations.any { it.hasReturnPath() }
            is WhileLoop -> this.hasGuaranteedReturn()
            else -> false
        }
    }

    private fun WhileLoop.hasGuaranteedReturn(): Boolean {
        val check = this.check

        if(this.body.hasReturnPath()) {
            return true
        }

        if(check is DataTypeInt && check.value != 0L) {
            //Special case of looping forever
            return !this.hasBreak()
        }
        return false
    }

    private fun Computable.hasBreak(): Boolean = when(this) {
        is BreakStatement -> true
        is WhileLoop, is FunctionDeclaration -> false
        else -> this.getComponents().any { it.hasBreak() }
    }
}