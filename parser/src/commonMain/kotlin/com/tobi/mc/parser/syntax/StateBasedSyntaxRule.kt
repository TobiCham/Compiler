package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.util.getComponents

internal abstract class StateBasedSyntaxRule<State> : InstanceSyntaxRule<Program>(Program::class) {

    final override fun Program.validate() {
        val initialState = getInitialState(this.context)
        this.traverse(initialState)
    }

    private fun Computable.traverse(state: State) {
        this.validate(state)

        val nextState = this.getNextState(state)
        for(component in getComponents()) {
            component.traverse(nextState)
        }
    }

    abstract fun Computable.validate(state: State)

//    /**
//     * Called once all this computable's children have been processed
//     */
//    fun onFinishedProcessing(computable: Computable, state: State) {}

    abstract fun Computable.getNextState(previousState: State): State

    abstract fun getInitialState(context: Context): State
}