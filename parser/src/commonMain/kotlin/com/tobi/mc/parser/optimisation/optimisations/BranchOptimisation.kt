package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object BranchOptimisation : Optimisation<Computable> {

    override val description: DescriptionMeta = SimpleDescription("Unnecessary branch removal", """
        Some if statements or while loops can be be determined to either always or never happen.
        Performs the following optimisations:
        
        if(0) {...} -> The else branch
        if(n) {...} -> The if branch [where n is any integer != 0]
        
        while(0) {...} -> Nothing
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is IfStatement || computable is WhileLoop

    override fun Computable.optimise(replace: (Computable) -> Boolean): Boolean {
        if(this is IfStatement) {
            return when(checkType(this.check)) {
                CheckType.SUCCEED -> replace(ifBody)
                CheckType.FAIL -> replace(elseBody ?: ExpressionSequence(emptyList()))
                CheckType.UNKNOWN -> false
            }
        }
        if(this is WhileLoop) {
            return when(checkType(this.check)) {
                CheckType.FAIL -> replace(ExpressionSequence(emptyList()))
                else -> false
            }
        }
        return false
    }

    private fun checkType(computable: DataComputable) = when {
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