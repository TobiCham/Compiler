package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.operation.Add
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Subtract
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTOptimisation
import com.tobi.mc.parser.util.isZero
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object AddZeroOptimisation : ASTOptimisation {

    override val description: DescriptionMeta = SimpleDescription("Add Zero Identity", """
        Optimises expressions of the form:
         - x ± 0
         - 0 ± x
        To simply x
    """.trimIndent())

    override fun optimise(item: Computable): OptimisationResult<Computable> {
        if(!(item is Add || item is Subtract)) {
            return noOptimisation()
        }
        item as MathOperation
        return when {
            item.arg1.isZero() -> newValue(item.arg2)
            item.arg2.isZero() -> newValue(item.arg1)
            else -> noOptimisation()
        }
    }
}