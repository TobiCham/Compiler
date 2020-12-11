package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object NegationOptimisation : ASTInstanceOptimisation<Negation>(Negation::class) {

    override val description: DescriptionMeta = SimpleDescription("Negation", """
        Optimises negations of constants. E.g:
         - !0 -> 1
         - !5 -> 0
    """.trimIndent())

    override fun Negation.optimiseInstance(): OptimisationResult<DataTypeInt> = when(val negation = negation) {
        is DataTypeInt -> newValue(DataTypeInt(if (negation.value == 0L) 1L else 0L))
        else -> noOptimisation()
    }
}