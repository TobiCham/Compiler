package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object ConstantBranchOptimisation : ASTOptimisation {

    override val description: DescriptionMeta = SimpleDescription("Constant branch removal", """
        Some if statements or while loops can be be determined to either always or never occur.
        Performs the following optimisations:
        
        if(0) {...} -> The else branch
        if(n) {...} -> The if branch [where n is any integer != 0]
        
        while(0) {...} -> Nothing
    """.trimIndent())

    override fun optimise(item: Computable): OptimisationResult<Computable> = when(item) {
        is IfStatement -> optimiseIf(item)
        is WhileLoop -> optimiseWhile(item)
        else -> noOptimisation()
    }

    private fun optimiseIf(ifStatement: IfStatement): OptimisationResult<ExpressionSequence> = when(checkType(ifStatement.check)) {
        CheckType.SUCCEED -> newValue(ifStatement.ifBody)
        CheckType.FAIL -> newValue(ifStatement.elseBody ?: ExpressionSequence())
        CheckType.UNKNOWN -> noOptimisation()
    }

    private fun optimiseWhile(whileLoop: WhileLoop): OptimisationResult<ExpressionSequence> = when(checkType(whileLoop.check)) {
        CheckType.FAIL -> newValue(ExpressionSequence())
        else -> noOptimisation()
    }

    private fun checkType(computable: Computable) = when {
        computable !is DataTypeInt -> CheckType.UNKNOWN
        computable.value == 0L -> CheckType.FAIL
        else -> CheckType.SUCCEED
    }

    private enum class CheckType {
        SUCCEED,
        UNKNOWN,
        FAIL
    }
}