package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.control.BreakStatement
import com.tobi.mc.computable.control.ContinueStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.parser.syntax.StateBasedSyntaxRule
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

//State indicates whether inside a loop
internal object RuleBreakAndContinueMustBeInsideLoop : StateBasedSyntaxRule<Boolean>() {

    override val description: DescriptionMeta = SimpleDescription("'break' & 'continue' must be declared inside a loop", """
        Ensures that the 'break' and 'continue' keywords are only declared within a while loop
    """.trimIndent())

    override fun Computable.validate(state: Boolean) {
        if(!state) {
            if(this is BreakStatement) throw ParseException("Unexpected 'break' outside a while loop")
            if(this is ContinueStatement) throw ParseException("Unexpected 'continue' outside a while loop")
        }
    }

    override fun Computable.getNextState(previousState: Boolean) = when(this) {
        is FunctionDeclaration -> false
        is WhileLoop -> true
        else -> previousState
    }

    override fun getInitialState(context: Context): Boolean = false
}