package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

internal object TwoNumberOptimisation : InstanceOptimisation<MathOperation>(MathOperation::class) {

    override val description: DescriptionMeta = SimpleDescription("Mathematical operation on two integers", """
        Optimises any inline mathematical operations on two integers (+, -, *, /, %, ==, !=, >, <, >=, <=)
        E.g. 3 + 2 -> 6 
    """.trimIndent())

    override fun MathOperation.optimiseInstance(): Computable? = when {
        arg1 is DataTypeInt && arg2 is DataTypeInt -> DataTypeInt(computation((arg1 as DataTypeInt).value, (arg2 as DataTypeInt).value))
        else -> null
    }
}