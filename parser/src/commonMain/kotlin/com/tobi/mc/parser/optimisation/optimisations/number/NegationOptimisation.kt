package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Negation
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object NegationOptimisation : InstanceOptimisation<Negation>(Negation::class) {

    override val description: DescriptionMeta = SimpleDescription("Negation", """
        Optimises negations of constants
    """.trimIndent())

    override fun Negation.optimise(replace: (Computable) -> Boolean): Boolean {
        if(negation is DataTypeInt) {
            val value = (negation as DataTypeInt).value
            return replace(DataTypeInt(if(value == 0L) 1L else 0L))
        }
        return false
    }
}