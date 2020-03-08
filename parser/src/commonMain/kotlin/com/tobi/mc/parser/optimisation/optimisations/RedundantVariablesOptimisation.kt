package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.*
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.util.DescriptionMeta
import com.tobi.util.copyAndReplaceIndex

object RedundantVariablesOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant variable remover", """
        Removes any unused variables and function declarations
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        for((i, operation) in operations.withIndex()) {
            if(operation is DefineVariable || operation is FunctionDeclaration) {
                if(!ExpressionSequence(operations.subList(i + 1, operations.size)).usesVariable((operation as VariableReference).name)) {
                    val newOps = if(operation is DefineVariable) {
                        operations.copyAndReplaceIndex(i, operation.value)
                    } else {
                        operations.filterIndexed { index, _ -> index != i }
                    }
                    return replace(ExpressionSequence(newOps))
                }
            }
        }
        return false
    }

    private fun Computable.usesVariable(name: String): Boolean {
        if(this is GetVariable || this is SetVariable) {
            if((this as VariableReference).name == name) {
                return true
            }
        }
        if(this is FunctionDeclaration) {
            if(this.name == name || this.parameters.any { it.first == name }) {
                //The variable we're looking for has been redefined
                return false
            }
        }
        for(component in getComponents()) {
            if(component is DefineVariable || component is FunctionDeclaration) {
                if((component as VariableReference).name == name) {
                    //The variable we're looking for has been redefined
                    return false
                }
            }
            if(component.usesVariable(name)) {
                return true
            }
        }
        return false
    }
}