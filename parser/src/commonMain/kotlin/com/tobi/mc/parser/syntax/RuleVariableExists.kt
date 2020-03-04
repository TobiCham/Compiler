package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.*
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.util.DescriptionMeta

internal object RuleVariableExists : SyntaxRule<VariablesState> {

    override val description: DescriptionMeta = SimpleDescription("Variable name checking", """
        Ensures the following criteria are met with regards to variables:
         - A variable is defined when accessing it
         - A variable is defined when setting its value
         - A variable is not already defined in scope with the same name of a new variable which is being defined
         - A variable is not already defined in scope with the same name of a new function which is being defined
    """.trimIndent())

    override fun validate(computable: Computable, state: VariablesState) = when(computable) {
        is GetVariable -> state.ensureExists(computable.name)
        is SetVariable -> state.ensureExists(computable.name)
        is DefineVariable -> state.ensureCanBeDefined(computable.name)
        is FunctionDeclaration -> state.ensureCanBeDefined(computable.name)
        else -> Unit
    }

    override fun getNextState
                (computable: Computable, previousState: VariablesState) = when(computable) {
        is ExpressionSequence -> VariablesState(previousState)
        is DefineVariable -> previousState.define(computable.name)
        is FunctionDeclaration -> {
            previousState.define(computable.name)
            val newState = VariablesState(previousState)
            for ((paramName, _) in computable.parameters) {
                newState.define(paramName)
            }
            newState
        }
        else -> previousState
    }

    override fun getInitialState(defaultContext: DefaultContext): VariablesState {
        //TODO Maybe define all top level functions first?
        val parentScope = VariablesState(null)
        for(name in defaultContext.getVariables().keys) {
            parentScope.define(name)
        }
        return VariablesState(parentScope)
    }
}