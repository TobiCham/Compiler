package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object UnaryMinusOptimisation : InstanceOptimisation<UnaryMinus>(UnaryMinus::class) {
    override val description: DescriptionMeta = SimpleDescription("Unary minus", """
        Optimises expressions in the form -(-x) to x
    """.trimIndent())

    override fun UnaryMinus.optimise(replace: (Computable) -> Boolean): Boolean {
        val exp = this.expression
        return when(exp) {
            is DataTypeInt -> replace(DataTypeInt(-exp.value))
            is UnaryMinus -> replace(exp.expression)
            else -> false
        }
    }
}