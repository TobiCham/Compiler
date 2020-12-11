package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Equals
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.NotEquals
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object NegationOfEquals : ASTInstanceOptimisation<Negation>(Negation::class) {

    override val description: DescriptionMeta = SimpleDescription("Negation of equals", """
        Optimises expressions in the form:
            - !(a == b) to a != b
            - !(a != b) to a == b
    """.trimIndent())

    override fun Negation.optimiseInstance(): OptimisationResult<Computable> = when(val negation = negation) {
        is Equals -> newValue(NotEquals(negation.arg1, negation.arg2))
        is NotEquals -> newValue(Equals(negation.arg1, negation.arg2))
        else -> noOptimisation()
    }
}