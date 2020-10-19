package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Add
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Subtract
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.isZero
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object AddZeroOptimisation : Optimisation {

    override val description: DescriptionMeta = SimpleDescription("Add Zero Identity", """
        Optimises expressions of the form:
         - x ± 0
         - 0 ± x
        To simply x
    """.trimIndent())

    override fun optimise(computable: Computable): Computable? {
        if(!(computable is Add || computable is Subtract)) {
            return null
        }
        computable as MathOperation
        return when {
            computable.arg1.isZero() -> computable.arg2
            computable.arg2.isZero() -> computable.arg1
            else -> null
        }
    }
}