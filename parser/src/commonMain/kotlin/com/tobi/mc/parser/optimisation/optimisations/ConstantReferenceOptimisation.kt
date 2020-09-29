package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.updateComponentAtIndex
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.copyExceptIndex

internal object ConstantReferenceOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Constant Reference Optimisation", """
        Replaces references to constants with the constants themselves  
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        for ((i, operation) in this.operations.withIndex()) {
            if(operation !is DefineVariable) continue
            val value = operation.value

            if(value !is DataTypeInt && value !is DataTypeString) continue
            val isSet = this.hasSetters(i + 1, operation.name)
            if(isSet) continue

            this.replaceReferences(i + 1, operation.name, value as Data)
            this.operations = this.operations.copyExceptIndex(i)
            return true
        }
        return false
    }

    private fun Computable.replaceReferences(startIndex: Int, name: String, value: Data) {
        if(this is FunctionDeclaration) {
            if(this.name == name || this.parameters.any { it.name == name }) {
                //The variable we're looking for has been redefined
                return
            }
        }
        for((i, component) in getComponents().withIndex()) {
            if(i < startIndex) continue
            if(component is GetVariable && component.name == name) {
                this.updateComponentAtIndex(i, value)
            }
            if(component is DefineVariable || component is FunctionDeclaration) {
                if((component as VariableReference).name == name) {
                    //The variable we're looking for has been redefined
                    return
                }
            }
            component.replaceReferences(0, name, value)
        }
    }

    private fun Computable.hasSetters(startIndex: Int, name: String): Boolean {
        if(this is SetVariable && this.name == name) {
            return true
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
            if(component.hasSetters(0, name)) {
                return true
            }
        }
        return false
    }
}