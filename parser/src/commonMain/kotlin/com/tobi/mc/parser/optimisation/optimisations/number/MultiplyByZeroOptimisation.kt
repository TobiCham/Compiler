package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.Divide
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.isZero
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

internal object MultiplyByZeroOptimisation : Optimisation {

    override val description: DescriptionMeta = SimpleDescription("Multiply by 0 identity", """
        Optimises expressions of the form:
        - 0 * x
        - x * 0
        - 0 / x
        To 0
    """.trimIndent())

    override fun optimise(computable: Computable): Computable? = when(computable) {
        is Multiply, is Divide -> optimise(computable as MathOperation)
        else -> null
    }

    private fun optimise(math: MathOperation): Computable? = when {
        math.arg1.isZero() && isValid(math.arg2) -> DataTypeInt(0)
        math is Multiply && math.arg2.isZero() && isValid(math.arg1) -> DataTypeInt(0)
        else -> null
    }

    private fun isValid(computable: Computable) = computable is DataTypeInt || computable is GetVariable
}