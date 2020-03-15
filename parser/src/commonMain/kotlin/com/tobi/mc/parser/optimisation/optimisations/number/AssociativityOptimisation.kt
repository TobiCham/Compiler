package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Add
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.MathOperation
import com.tobi.mc.computable.Multiply
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object AssociativityOptimisation : Optimisation<MathOperation> {

    override val description: DescriptionMeta = SimpleDescription("Associativity Optimisation", """
        Optimises sequences of the form:
        a + b + c + ...
        and
        a * b * c * ...
        
        By reducing all constants
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is Add || computable is Multiply

    override fun MathOperation.optimise(replace: (Computable) -> Boolean): Boolean {
        val result = if(this is Add) {
            val reducer = AssociativityReducer(Add::class, ::Add, Long::plus)
            reducer.reduce(this)
        } else {
            val reducer = AssociativityReducer(Multiply::class, ::Multiply, Long::times)
            reducer.reduce(this as Multiply)
        }
        if(result !== this) {
            return replace(result)
        }
        return false
    }
}