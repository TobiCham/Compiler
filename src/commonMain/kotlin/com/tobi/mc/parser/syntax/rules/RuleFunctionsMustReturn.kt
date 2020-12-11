package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.BreakStatement
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RuleFunctionsMustReturn : InstanceSyntaxRule<FunctionDeclaration>(FunctionDeclaration::class) {

    override val description: DescriptionMeta = SimpleDescription("Functions must return", """
        Ensures that there is a return expression on all possible paths for non void functions
    """.trimIndent())

    override fun FunctionDeclaration.validateInstance() {
        val returnStatements = ArrayList<ReturnStatement>()
        val hasAllReturns = body.hasAllReturnPaths(returnStatements)

        val returnsValue = returnStatements.any { it.toReturn != null }

        if(returnsValue && returnType == DataType.VOID) {
            throw ParseException("Cannot return a value in a void function", returnStatements.first { it.toReturn != null })
        }
        if(returnsValue && returnStatements.any { it.toReturn == null }) {
            throw ParseException("Expected a return value", returnStatements.first { it.toReturn == null })
        }

        if((returnsValue || !(returnType == DataType.VOID || returnType == null)) && !hasAllReturns) {
            throw ParseException("Function must have a return on all possible paths", this)
        }
    }

    private fun Computable.hasAllReturnPaths(returns: MutableList<ReturnStatement>): Boolean = when(this) {
        is ReturnStatement -> {
            returns.add(this)
            true
        }
        is IfStatement -> elseBody != null && this.ifBody.hasAllReturnPaths(returns) && elseBody!!.hasAllReturnPaths(returns)
        is ExpressionSequence -> expressions.any { it.hasAllReturnPaths(returns) }
        is WhileLoop -> this.hasGuaranteedReturn(returns)
        else -> false
    }

    private fun WhileLoop.hasGuaranteedReturn(returns: MutableList<ReturnStatement>): Boolean {
        val check = this.check

        if(this.body.hasAllReturnPaths(returns)) {
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
        else -> this.getNodes().any { it.hasBreak() }
    }
}