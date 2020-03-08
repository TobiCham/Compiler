package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.MathOperation
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isZero
import com.tobi.util.DescriptionMeta

internal object AddZeroOptimisation : InstanceOptimisation<MathOperation>(MathOperation::class) {

    override val description: DescriptionMeta = SimpleDescription("Add Zero Identity", """
        Optimises expressions of the form:
         - x ± 0
         - 0 ± x
        To simply x
    """.trimIndent())

    override fun MathOperation.optimise(replace: (Computable) -> Boolean) = when {
        arg1.isZero() -> replace(arg2)
        arg2.isZero() -> replace(arg1)
        else -> false
    }
}