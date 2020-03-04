package com.tobi.mc.parser.syntax

import com.tobi.mc.ParseException
import com.tobi.mc.computable.*
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.util.DescriptionMeta

//State indicates whether inside a loop
internal object RuleBreakAndContinueMustBeInsideLoop : SyntaxRule<Boolean> {

    override val description: DescriptionMeta = SimpleDescription("'break' & 'continue' must be declared inside a loop", """
        Ensures that the 'break' and 'continue' keywords are only declared within a while loop
    """.trimIndent())

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

    override fun getInitialState(defaultContext: DefaultContext): Boolean = false
}