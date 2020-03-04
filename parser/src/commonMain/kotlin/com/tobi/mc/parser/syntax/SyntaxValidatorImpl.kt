package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DefaultContext
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.SyntaxValidator
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.util.DescriptionMeta

internal class SyntaxValidatorImpl(private val validators: Array<SyntaxRule<*>>) : SyntaxValidator {

    override val description: DescriptionMeta = SimpleDescription("Syntax Validator", """
        Validates that the program syntax is valid
    """.trimIndent())

    override val syntaxRuleDescriptions: List<DescriptionMeta> = validators.map(SyntaxRule<*>::description)

    override fun processProgram(program: Program): Program {
        validateProgram(program, DefaultContext())
        return program
    }

    override fun validateSyntax(computable: Computable, defaultContext: DefaultContext) {
        val stateAndRules = validators.map {
            SyntaxRuleState.fromInitial(it, defaultContext)
        }
        traverseTree(computable, stateAndRules)
    }

    override fun validateProgram(program: Program, defaultContext: DefaultContext) {
        val stateAndRules = validators.map {
            SyntaxRuleState.fromInitial(it, defaultContext)
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
        for (component in computable.getComponents()) {
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
            fun <State> fromInitial(rule: SyntaxRule<State>, defaultContext: DefaultContext): SyntaxRuleState<State> {
                return SyntaxRuleState(rule, rule.getInitialState(defaultContext))
            }
        }
    }
}