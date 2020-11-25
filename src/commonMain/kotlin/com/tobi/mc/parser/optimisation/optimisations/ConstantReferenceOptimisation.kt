package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.parser.util.updateComponentAtIndex
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object ConstantReferenceOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Constant Reference Optimisation", """
        Replaces references to variables who's value is a constant and is not modified to the value itself  
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val variablesToInline = ArrayList<Computable>()
        for(operation in this.operations) {
            if(operation !is VariableDeclaration) {
                continue
            }
            val value = operation.value
            if(value !is Data || !(value is DataTypeInt || value is DataTypeString)) {
                continue
            }
            if(this.hasSetters(operation.name)) {
                continue
            }
            this.updateGetters(operation.name, value)
            variablesToInline.add(operation)
        }
        if(variablesToInline.isEmpty()) {
            return null
        }
        val newOps = this.operations.filter { op -> !variablesToInline.contains(op) }
        return ExpressionSequence(newOps)
    }

    private fun ExpressionSequence.updateGetters(targetName: String, value: Data) {
        return this.traverseWithDepth().forEach { (computable, depth) ->
            computable.updateGetter(depth, targetName, value)
        }
    }

    private fun Computable.updateGetter(depth: Int, targetName: String, value: Data) {
        for ((i, component) in this.getComponents().withIndex()) {
            if(component is GetVariable && component.name == targetName && component.contextIndex == depth) {
                this.updateComponentAtIndex(i, value)
            }
        }
    }

    private fun ExpressionSequence.hasSetters(targetName: String): Boolean {
        return this.traverseWithDepth().any { (computable, depth) ->
            computable is SetVariable && computable.name == targetName && computable.contextIndex == depth - 1
        }
    }
}