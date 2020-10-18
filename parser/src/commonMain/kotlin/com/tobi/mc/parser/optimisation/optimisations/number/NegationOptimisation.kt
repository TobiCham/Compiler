package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

internal object NegationOptimisation : InstanceOptimisation<Negation>(Negation::class) {

    override val description: DescriptionMeta = SimpleDescription("Negation", """
        Optimises negations of constants
    """.trimIndent())

    override fun Negation.optimiseInstance(): Computable? = when(val negation = negation) {
        is DataTypeInt -> DataTypeInt(if(negation.value == 0L) 1L else 0L)
        else -> null
    }
}