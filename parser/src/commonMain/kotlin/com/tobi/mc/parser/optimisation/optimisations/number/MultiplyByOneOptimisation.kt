package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Divide
import com.tobi.mc.computable.MathOperation
import com.tobi.mc.computable.Multiply
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isOne
import com.tobi.mc.util.DescriptionMeta

internal object MultiplyByOneOptimisation : Optimisation<MathOperation> {

    override val description: DescriptionMeta = SimpleDescription("Multiply by 1 identity", """
        Optimises expressions of the form:
         - 1 * x
         - x * 1
         - x / 1
         To simply x
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is Multiply || computable is Divide

    override fun MathOperation.optimise(replace: (Computable) -> Boolean) = when {
        arg2.isOne() -> replace(arg1)
        this is Multiply && arg1.isOne() -> replace(arg2)
        else -> false
    }
}