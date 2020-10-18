package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

internal object BranchOptimisation : Optimisation {

    override val description: DescriptionMeta = SimpleDescription("Unnecessary branch removal", """
        Some if statements or while loops can be be determined to either always or never happen.
        Performs the following optimisations:
        
        if(0) {...} -> The else branch
        if(n) {...} -> The if branch [where n is any integer != 0]
        
        while(0) {...} -> Nothing
    """.trimIndent())

    override fun optimise(computable: Computable): Computable? = when(computable) {
        is IfStatement -> optimiseIf(computable)
        is WhileLoop -> optimiseWhile(computable)
        else -> null
    }

    private fun optimiseIf(ifStatement: IfStatement): Computable? = when(checkType(ifStatement.check)) {
        CheckType.SUCCEED -> ifStatement.ifBody
        CheckType.FAIL -> ifStatement.elseBody ?: ExpressionSequence(emptyList())
        CheckType.UNKNOWN -> null
    }

    private fun optimiseWhile(whileLoop: WhileLoop): Computable? = when(checkType(whileLoop.check)) {
        CheckType.FAIL -> ExpressionSequence(emptyList())
        else -> null
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