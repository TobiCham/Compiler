package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DefaultContext

internal interface SimpleRule : SyntaxRule<Unit> {

    override fun getNextState(computable: Computable, previousState: Unit) = previousState

    override fun getInitialState(defaultContext: DefaultContext) = Unit
}