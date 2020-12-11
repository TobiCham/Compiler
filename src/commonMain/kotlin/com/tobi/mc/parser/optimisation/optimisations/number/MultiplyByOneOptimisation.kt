package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Divide
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTOptimisation
import com.tobi.mc.parser.util.isOne
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object MultiplyByOneOptimisation : ASTOptimisation {

    override val description: DescriptionMeta = SimpleDescription("Multiply by 1 identity", """
        Optimises expressions of the form:
         - 1 * x
         - x * 1
         - x / 1
         To simply x
    """.trimIndent())

    override fun optimise(item: Computable): OptimisationResult<Computable> = when(item) {
        is Multiply, is Divide -> optimise(item as MathOperation)
        else -> noOptimisation()
    }

    private fun optimise(math: MathOperation): OptimisationResult<Computable> = when {
        math.arg2.isOne() -> newValue(math.arg1)
        math is Multiply && math.arg1.isOne() -> newValue(math.arg2)
        else -> noOptimisation()
    }
}