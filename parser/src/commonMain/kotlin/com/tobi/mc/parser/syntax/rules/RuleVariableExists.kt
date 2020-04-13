package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.computable.*
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.parser.syntax.VariablesState
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta

internal object RuleVariableExists : InstanceSyntaxRule<Program>(Program::class) {

    override val description: DescriptionMeta = SimpleDescription("Variable name checking", """
        Ensures the following criteria are met with regards to variables:
         - A variable is defined when accessing it
         - A variable is defined when setting its value
         - A variable is not already defined in scope with the same name of a new variable which is being defined
         - A variable is not already defined in scope with the same name of a new function which is being defined
    """.trimIndent())

    override fun Program.validate() {
        val parentState = VariablesState(null)
        for(name in this.context.getVariables().keys) {
            parentState.define(name)
        }
        this.code.validate(parentState)
    }

    private fun Computable.validate(state: VariablesState) {
        var newState = state
        if(this is VariableReference) {
            this.validateReference(state)
        }

        when(this) {
            is ExpressionSequence -> newState = VariablesState(state)
            is DefineVariable -> state.define(name)
            is FunctionDeclaration -> {
                state.define(name)
                newState = VariablesState(state)
                for ((_, paramName) in parameters) {
                    newState.define(paramName)
                }
            }
        }
        for(component in this.getComponents()) {
            component.validate(newState)
        }
        if(this is DefineVariable) {
            state.define(this.name)
        }
    }

    private fun VariableReference.validateReference(state: VariablesState) = when(this) {
        is GetVariable, is SetVariable -> state.ensureExists(name)
        is DefineVariable -> state.ensureCanBeDefined(name)
        is FunctionDeclaration -> state.ensureCanBeDefined(name)
        else -> Unit
    }
}