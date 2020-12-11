package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RedundantVariablesOptimisation : ASTInstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant variable remover", """
        Removes any unused variables and function declarations
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): OptimisationResult<ExpressionSequence> {
        val unusedSymbols = ArrayList<Computable>()
        for (expression in this.getNodes()) {
            if(expression is FunctionPrototype || expression is VariableDeclaration) {
                if(!this.hasUsages((expression as VariableReference).name)) {
                    unusedSymbols.add(expression)
                    if(expression is FunctionPrototype) {
                        unusedSymbols.add(expression.function)
                    }
                }
            }
        }
        if(unusedSymbols.isEmpty()) {
            return noOptimisation()
        }
        val newOps = this.expressions.mapNotNull { op ->
            if(unusedSymbols.contains(op)) {
                if(op is FunctionDeclaration) null
                else if(op is VariableDeclaration) op.value
                else throw IllegalStateException(op::class.simpleName)
            } else op
        }
        return newValue(ExpressionSequence(newOps.toMutableList()))
    }

    private fun ExpressionSequence.hasUsages(targetName: String): Boolean {
        return this.traverseWithDepth().any { (computable, depth) ->
            computable is VariableContext && computable.name == targetName && computable.contextIndex == depth - 1
        }
    }
}