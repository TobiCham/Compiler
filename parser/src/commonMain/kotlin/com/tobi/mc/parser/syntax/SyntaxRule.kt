package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DefaultContext
import com.tobi.util.DescriptionMeta

internal interface SyntaxRule<State> {

    val description: DescriptionMeta

    fun validate(computable: Computable, state: State)

    /**
     * Called once all this computable's children have been processed
     */
    fun onFinishedProcessing(computable: Computable, state: State) {}

    fun getNextState(computable: Computable, previousState: State): State

    fun getInitialState(defaultContext: DefaultContext): State
}