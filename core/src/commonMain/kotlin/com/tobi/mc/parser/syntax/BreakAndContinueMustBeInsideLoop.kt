package com.tobi.mc.parser.syntax

import com.tobi.mc.DefaultContext
import com.tobi.mc.computable.*
import com.tobi.mc.parser.ParseException

//State indicates whether inside a loop
object BreakAndContinueMustBeInsideLoop : SyntaxRule<Boolean> {

    override fun validate(computable: Computable, state: Boolean) {
        if(!state) {
            if(computable is BreakStatement) throw ParseException("Unexpected 'break' outside a while loop")
            if(computable is ContinueStatement) throw ParseException("Unexpected 'continue' outside a while loop")
        }
    }

    override fun getNextState(computable: Computable, previousState: Boolean) = when(computable) {
        is FunctionDeclaration -> false
        is WhileLoop -> true
        else -> previousState
    }

    override fun getInitialState(program: Program, defaultContext: DefaultContext): Boolean = false
}