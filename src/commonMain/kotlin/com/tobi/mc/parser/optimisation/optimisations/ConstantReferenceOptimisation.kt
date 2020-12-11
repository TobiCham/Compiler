package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.parser.util.updateNodeAtIndex
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object ConstantReferenceOptimisation : ASTInstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Constant Reference Optimisation", """
        Replaces references to final variables who's value is a constant to the value itself  
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): OptimisationResult<ExpressionSequence> {
        val variablesToInline = ArrayList<Computable>()
        for(expression in this.expressions) {
            if(expression !is VariableDeclaration) {
                continue
            }
            val value = expression.value
            if(value !is Data || !(value is DataTypeInt || value is DataTypeString)) {
                continue
            }
            if(this.hasSetters(expression.name)) {
                continue
            }
            this.updateGetters(expression.name, value)
            variablesToInline.add(expression)
        }
        if(variablesToInline.isEmpty()) {
            return noOptimisation()
        }
        val newOps = this.expressions.filter { op -> !variablesToInline.contains(op) }
        return newValue(ExpressionSequence(newOps.toMutableList()))
    }

    private fun ExpressionSequence.updateGetters(targetName: String, value: Data) {
        return this.traverseWithDepth().forEach { (computable, depth) ->
            computable.updateGetter(depth, targetName, value)
        }
    }

    private fun Computable.updateGetter(depth: Int, targetName: String, value: Data) {
        val newDepth = if(this is ExpressionSequence || this is FunctionDeclaration) depth + 1 else depth
        for ((i, node) in this.getNodes().withIndex()) {
            if(node is GetVariable && node.name == targetName) {
                if(node.contextIndex == newDepth - 1) {
                    this.updateNodeAtIndex(i, value)
                }
            }
        }
    }

    private fun ExpressionSequence.hasSetters(targetName: String): Boolean {
        return this.traverseWithDepth().any { (computable, depth) ->
            computable is SetVariable && computable.name == targetName && computable.contextIndex == depth - 1
        }
    }
}