package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Add
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object AssociativityOptimisation : ASTOptimisation {

    override val description: DescriptionMeta = SimpleDescription("Associativity Optimisation", """
        Optimises sequences of the form:
        a + b + 1 + c + 2 + ... -> 3 + a + b + c + ...
        and
        a * b * 2 * c * 3 * ... -> 6 * a * b * c * ...
    """.trimIndent())

    override fun optimise(item: Computable): OptimisationResult<Computable> = when(item) {
        is Add -> AssociativityReducer(Add::class, ::Add, Long::plus).reduce(item)
        is Multiply -> AssociativityReducer(Multiply::class, ::Multiply, Long::times).reduce(item)
        else -> noOptimisation()
    }
}