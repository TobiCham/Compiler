package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RedundantOperationOptimisation : ASTInstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription(
        "Redundant Expression Removal", """
        Removes redundant expressions, e.g:
        5;
        "hello";
        {}
        are removed
    """.trimIndent()
    )

    override fun ExpressionSequence.optimiseInstance(): OptimisationResult<ExpressionSequence> {
        val newOperations = ArrayList<Computable>(this.expressions.size)
        var modified = false
        for(expression in this.expressions) {
            if(!isRequired(expression)) {
                modified = true

                //Add all child nodes so they still get executed
                newOperations.addAll(expression.getNodes())
            } else {
                newOperations.add(expression)
            }
        }
        if(!modified) {
            return noOptimisation()
        }
        return newValue(ExpressionSequence(newOperations))
    }

    private fun isRequired(computable: Computable): Boolean = when {
        //Some other optimisations may return an empty sequence within this sequence
        computable is ExpressionSequence && computable.expressions.isEmpty() -> false
        computable is Data -> false
        computable is MathOperation -> false
        computable is Negation -> false
        computable is UnaryMinus -> false
        computable is StringConcat -> false
        else -> true
    }
}