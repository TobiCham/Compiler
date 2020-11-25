package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RedundantOperationOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription(
        "Redundant Expression Removal", """
        Removes redundant expressions, e.g:
        5;
        "hello";
        {}
        are removed
    """.trimIndent()
    )

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val newOperations = ArrayList<Computable>(this.operations.size)
        var modified = false
        for(operation in this.operations) {
            if(!isRequired(operation)) {
                modified = true

                //Add all child components so they still get executed
                newOperations.addAll(operation.getComponents())
            } else {
                newOperations.add(operation)
            }
        }
        if(!modified) {
            return null
        }
        return ExpressionSequence(newOperations)
    }

    private fun isRequired(computable: Computable): Boolean = when {
        //Some other optimisations may return an empty sequence within this sequence
        computable is ExpressionSequence && computable.operations.isEmpty() -> false
        computable is Data -> false
        computable is MathOperation -> false
        computable is Negation -> false
        computable is UnaryMinus -> false
        computable is StringConcat -> false
        else -> true
    }
}