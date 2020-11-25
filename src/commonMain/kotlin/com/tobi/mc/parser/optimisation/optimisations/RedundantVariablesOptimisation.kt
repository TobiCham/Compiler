package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RedundantVariablesOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant variable remover", """
        Removes any unused variables and function declarations
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val unusedSymbols = ArrayList<Computable>()
        for (operation in this.getComponents()) {
            if(operation is FunctionPrototype || operation is VariableDeclaration) {
                if(!this.hasUsages((operation as VariableReference).name)) {
                    unusedSymbols.add(operation)
                    if(operation is FunctionPrototype) {
                        unusedSymbols.add(operation.function)
                    }
                }
            }
        }
        if(unusedSymbols.isEmpty()) {
            return null
        }
        val newOps = this.operations.mapNotNull { op ->
            if(unusedSymbols.contains(op)) {
                if(op is FunctionDeclaration) null
                else if(op is VariableDeclaration) op.value
                else throw IllegalStateException(op::class.simpleName)
            } else op
        }
        return ExpressionSequence(newOps)
    }

    private fun ExpressionSequence.hasUsages(targetName: String): Boolean {
        return this.traverseWithDepth().any { (computable, depth) ->
            computable is VariableContext && computable.name == targetName && computable.contextIndex == depth - 1
        }
    }
}