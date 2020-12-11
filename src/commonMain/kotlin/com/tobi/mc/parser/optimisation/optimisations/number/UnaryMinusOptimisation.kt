package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object UnaryMinusOptimisation : ASTInstanceOptimisation<UnaryMinus>(UnaryMinus::class) {
    override val description: DescriptionMeta = SimpleDescription("Unary minus", """
        Optimises expressions in the form -(-x) to x
    """.trimIndent())

    override fun UnaryMinus.optimiseInstance(): OptimisationResult<Computable> = when(val expression = expression) {
        is DataTypeInt -> newValue(DataTypeInt(-expression.value))
        is UnaryMinus -> newValue(expression.expression)
        else -> noOptimisation()
    }
}