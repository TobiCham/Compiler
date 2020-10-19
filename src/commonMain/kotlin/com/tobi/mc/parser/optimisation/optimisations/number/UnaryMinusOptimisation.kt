package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object UnaryMinusOptimisation : InstanceOptimisation<UnaryMinus>(UnaryMinus::class) {
    override val description: DescriptionMeta = SimpleDescription("Unary minus", """
        Optimises expressions in the form -(-x) to x
    """.trimIndent())

    override fun UnaryMinus.optimiseInstance(): Computable? = when(val expression = expression) {
        is DataTypeInt -> DataTypeInt(-expression.value)
        is UnaryMinus -> expression.expression
        else -> null
    }
}