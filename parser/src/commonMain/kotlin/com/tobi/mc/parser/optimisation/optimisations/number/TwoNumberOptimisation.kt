package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.MathOperation
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.util.DescriptionMeta

internal object TwoNumberOptimisation : Optimisation<MathOperation> {

    override val description: DescriptionMeta = SimpleDescription("Mathematical operation on two integers", """
        Optimises any inline mathematical operations on two integers (+, -, *, /, %, ==, !=, >, <, >=, <=)
        E.g. (1 + 2) * 2 -> 6 
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is MathOperation

    override fun MathOperation.optimise(replace: (Computable) -> Boolean) = when {
        arg1 is DataTypeInt && arg2 is DataTypeInt -> replace(DataTypeInt(computation((arg1 as DataTypeInt).value, (arg2 as DataTypeInt).value)))
        else -> false
    }
}