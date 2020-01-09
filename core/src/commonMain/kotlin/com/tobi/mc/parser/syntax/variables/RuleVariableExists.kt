package com.tobi.mc.parser.syntax.variables

import com.tobi.mc.DefaultContext
import com.tobi.mc.computable.*
import com.tobi.mc.parser.syntax.SyntaxRule

object RuleVariableExists : SyntaxRule<VariablesState> {

    override fun validate(computable: Computable, state: VariablesState) = when(computable) {
        is GetVariable -> state.ensureExists(computable.name)
        is SetVariable -> state.ensureExists(computable.name)
        is DefineVariable -> state.ensureCanBeDefined(computable.name)
        is FunctionDeclaration -> state.ensureCanBeDefined(computable.name)
        else -> Unit
    }

    override fun getNextState(computable: Computable, previousState: VariablesState) = when(computable) {
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

    override fun getInitialState(program: Program, defaultContext: DefaultContext): VariablesState {
        //TODO Maybe define all top level functions first?
        val parentScope = VariablesState(null)
        for(name in defaultContext.getVariables().keys) {
            parentScope.define(name)
        }
        return VariablesState(parentScope)
    }
}