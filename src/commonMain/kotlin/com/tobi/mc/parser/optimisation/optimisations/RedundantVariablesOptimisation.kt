package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.*
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription
import com.tobi.mc.util.copyAndReplaceIndex

object RedundantVariablesOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant variable remover", """
        Removes any unused variables and function declarations
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val unusedSymbols = ArrayList<Computable>()
        for (operation in this.getComponents()) {
            if(operation is FunctionPrototype || operation is DefineVariable) {
                if(!operation.hasUsages(0, (operation as VariableReference).name)) {
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
        val newOps = this.operations.filter { op -> !unusedSymbols.contains(op) }
        return ExpressionSequence(newOps)
    }

    private fun Computable.hasUsages(contextIndex: Int, targetName: String): Boolean {
        if(this is VariableContext) {
            return this.contextIndex == contextIndex && this.name == targetName
        }
        if(this is ExpressionSequence) {
            return this.getComponents().any {
                it.hasUsages(contextIndex + 1, targetName)
            }
        }
        if(this is FunctionDeclaration) {
            return this.body.hasUsages(contextIndex + 1, targetName)
        }
        return this.getComponents().any {
            it.hasUsages(contextIndex + 1, targetName)
        }
    }
}