package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isZero
import com.tobi.mc.util.DescriptionMeta

internal object MultiplyByZeroOptimisation : Optimisation<MathOperation> {

    override val description: DescriptionMeta = SimpleDescription("Multiply by 0 identity", """
        Optimises expressions of the form:
        - 0 * x
        - x * 0
        - 0 / x
        To 0
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is Multiply || computable is Divide

    override fun MathOperation.optimise(replace: (Computable) -> Boolean) = when {
        arg1.isZero() && isValid(arg2) -> replace(DataTypeInt(0))
        this is Multiply && arg2.isZero() && isValid(arg1) -> replace(DataTypeInt(0))
        else -> false
    }

    private fun isValid(computable: Computable) = computable is DataTypeInt || computable is GetVariable
}