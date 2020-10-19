package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Divide
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.isOne
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object MultiplyByOneOptimisation : Optimisation {

    override val description: DescriptionMeta = SimpleDescription("Multiply by 1 identity", """
        Optimises expressions of the form:
         - 1 * x
         - x * 1
         - x / 1
         To simply x
    """.trimIndent())

    override fun optimise(computable: Computable): Computable? = when(computable) {
        is Multiply, is Divide -> optimise(computable as MathOperation)
        else -> null
    }

    private fun optimise(math: MathOperation): Computable? = when {
        math.arg2.isOne() -> math.arg1
        math is Multiply && math.arg1.isOne() -> math.arg2
        else -> null
    }
}