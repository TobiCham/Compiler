package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.copyAndReplaceIndex

internal object RedundantVariablesOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant variable remover", """
        Removes any unused variables and function declarations
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        for ((i, operation) in this.operations.withIndex()) {
            if(hasUsages(i, operation)) {
                continue
            }
            val newOps = if(operation is DefineVariable) {
                operations.copyAndReplaceIndex(i, operation.value)
            } else {
                operations.filterIndexed { index, _ -> index != i }
            }
            return replace(ExpressionSequence(newOps))
        }
        return false
    }

    private fun ExpressionSequence.hasUsages(index: Int, computable: Computable): Boolean {
        if(computable !is DefineVariable && computable !is FunctionDeclaration) {
            return true
        }
        return this.usesVariable(index + 1, (computable as VariableReference).name)
    }


    private fun Computable.usesVariable(startIndex: Int, name: String): Boolean {
        if(this is GetVariable || this is SetVariable) {
            if((this as VariableReference).name == name) {
                return true
            }
        }
        if(this is FunctionDeclaration) {
            if(this.name == name || this.parameters.any { it.name == name }) {
                //The variable we're looking for has been redefined
                return false
            }
        }
        for((i, component) in getComponents().withIndex()) {
            if(i < startIndex) continue
            if(component is DefineVariable || component is FunctionDeclaration) {
                if((component as VariableReference).name == name) {
                    //The variable we're looking for has been redefined
                    return false
                }
            }
            if(component.usesVariable(0, name)) {
                return true
            }
        }
        return false
    }
}