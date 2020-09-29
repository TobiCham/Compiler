package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Equals
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.NotEquals
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object NegationOfEquals : InstanceOptimisation<Negation>(Negation::class) {

    override val description: DescriptionMeta = SimpleDescription("Negation of equals", """
        Optimises expressions in the form !(a == b) to a != b
    """.trimIndent())

    override fun Negation.optimise(replace: (Computable) -> Boolean): Boolean {
        if(negation !is Equals) return false
        val negation = negation as Equals
        return replace(NotEquals(negation.arg1, negation.arg2))
    }
}