package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Add
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object AssociativityOptimisation : Optimisation {

    override val description: DescriptionMeta = SimpleDescription("Associativity Optimisation", """
        Optimises sequences of the form:
        a + b + c + ...
        and
        a * b * c * ...
        
        By reducing all constants
    """.trimIndent())

    override fun optimise(computable: Computable): Computable? = when(computable) {
        is Add -> AssociativityReducer(Add::class, ::Add, Long::plus).reduce(computable)
        is Multiply -> AssociativityReducer(Multiply::class, ::Multiply, Long::times).reduce(computable)
        else -> null
    }
}