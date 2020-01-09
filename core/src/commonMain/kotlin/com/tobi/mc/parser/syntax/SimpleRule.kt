package com.tobi.mc.parser.syntax

import com.tobi.mc.DefaultContext
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program

interface SimpleRule : SyntaxRule<Unit> {

    override fun getNextState(computable: Computable, previousState: Unit) = previousState

    override fun getInitialState(program: Program, defaultContext: DefaultContext) = Unit
}