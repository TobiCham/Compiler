package com.tobi.mc.parser.syntax

import com.tobi.mc.DefaultContext
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program

class SyntaxValidator(private vararg val validators: SyntaxRule<*>) {

    fun validate(program: Program, defaultContext: DefaultContext) {
        val stateAndRules = validators.map {
            SyntaxRuleState.fromInitial(it, program, defaultContext)
        }

        for(function in program) {
            traverseTree(function, stateAndRules)
        }
    }

    private fun traverseTree(computable: Computable, states: List<SyntaxRuleState<*>>) {
        if(states.isEmpty()) return

        val newStates = ArrayList<SyntaxRuleState<*>>()
        for(state in states) {
            state.validate(computable)
            val newState = state.nextState(computable)
            if(newState != null) {
                newStates.add(newState)
            }
        }
        for (component in computable.components) {
            traverseTree(component, newStates)
        }
        for(state in states) {
            state.onFinish(computable)
        }
    }

    private data class SyntaxRuleState<State>(val rule: SyntaxRule<State>, val state: State) {

        fun nextState(computable: Computable): SyntaxRuleState<State>? {
            val nextState = rule.getNextState(computable, state)
            return SyntaxRuleState(rule, nextState)
        }

        fun validate(computable: Computable) = rule.validate(computable, state)
        fun onFinish(computable: Computable) = rule.onFinishedProcessing(computable, state)

        companion object {
            fun <State> fromInitial(rule: SyntaxRule<State>, program: Program, defaultContext: DefaultContext): SyntaxRuleState<State> {
                return SyntaxRuleState(rule, rule.getInitialState(program, defaultContext))
            }
        }
    }
}