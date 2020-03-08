package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Data
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.util.DescriptionMeta

internal object RedundantOperationOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant Expression Removal", """
        Removes redundant expressions, e.g:
        5;
        "hello";
        [empty statement]
        are removed
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        val newOperations = this.operations.filter(this@RedundantOperationOptimisation::isRequired)
        if(newOperations.size == this.operations.size) {
            //No operations were redundant
            return false
        }
        return replace(ExpressionSequence(newOperations))
    }

    private fun isRequired(computable: Computable): Boolean = when {
        //Some other optimisations may return an empty sequence within this sequence
        computable is ExpressionSequence && computable.operations.isEmpty() -> false
        computable is Data -> false
        else -> true
    }
}