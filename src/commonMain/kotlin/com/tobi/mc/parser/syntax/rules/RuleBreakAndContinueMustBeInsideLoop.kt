package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.BreakStatement
import com.tobi.mc.computable.control.ContinueStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RuleBreakAndContinueMustBeInsideLoop : InstanceSyntaxRule<Program>(Program::class) {

    override val description: DescriptionMeta = SimpleDescription("'break' & 'continue' must be declared inside a loop", """
        Ensures that the 'break' and 'continue' keywords are only declared within a while loop
    """.trimIndent())

    override fun Program.validateInstance() {
        validate(this, false)
    }

    private fun validate(computable: Computable, isInLoop: Boolean) {
        if(!isInLoop) {
            if(computable is BreakStatement) throw ParseException("Unexpected 'break' outside a while loop", computable)
            if(computable is ContinueStatement) throw ParseException("Unexpected 'continue' outside a while loop", computable)
        }
        val nextInLoop = when(computable) {
            is FunctionDeclaration -> false
            is WhileLoop -> true
            else -> isInLoop
        }
        for(component in computable.getComponents()) {
            validate(component, nextInLoop)
        }
    }
}