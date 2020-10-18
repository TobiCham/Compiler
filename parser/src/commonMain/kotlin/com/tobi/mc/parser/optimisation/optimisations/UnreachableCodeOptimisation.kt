package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.isOne
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

internal object UnreachableCodeOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Unreachable code elimination", """
        Code can be removed when it is impossible for it to be executed. E.g.
        
        println("Hello");
        return;
        println("World");
        
        is optimised to:
        
        println("Hello");
        return;
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val interruptElement = this.operations.withIndex().firstOrNull {
            !canMovePast(it.value)
        } ?: return null

        if(interruptElement.index == this.operations.size - 1) {
            return null
        }
        return ExpressionSequence(this.operations.slice(0 until interruptElement.index + 1))
    }

    private fun canMovePast(computable: Computable) = when(computable) {
        is FlowInterruptComputable -> false
        is WhileLoop -> {
            val result = computable.getExitType() != ExitType.INFINITE_LOOP
            if (!result) println("INFINITE LOOP")
            result
        }
        else -> true
    }

    private fun Computable.getExitType(): ExitType = when(this) {
        is BreakStatement -> ExitType.BREAK
        is ContinueStatement -> ExitType.CONTINUE
        is ReturnStatement -> ExitType.RETURN
        is FunctionDeclaration -> ExitType.NONE
        is IfStatement -> {
            val b1 = this.ifBody.getExitType()
            val b2 = this.elseBody?.getExitType() ?: ExitType.NONE

            if(b1 == ExitType.RETURN || b2 == ExitType.RETURN) {
                ExitType.RETURN
            } else if(b1 == ExitType.BREAK || b2 == ExitType.BREAK) {
                ExitType.BREAK
            } else if(b1 == b2) {
                b1
            } else {
                ExitType.NONE
            }
        }
        is WhileLoop -> {
            val type = this.body.getExitType()
            val isUnconditional = this.check.isOne()
            when {
                type == ExitType.RETURN -> ExitType.RETURN
                type == ExitType.INFINITE_LOOP -> ExitType.INFINITE_LOOP
                (type == ExitType.NONE || type == ExitType.CONTINUE) && isUnconditional -> ExitType.INFINITE_LOOP
                else -> ExitType.NONE
            }
        }
        else -> this.getComponents().asSequence().map {
            it.getExitType()
        }.find {
            it != ExitType.NONE
        } ?: ExitType.NONE
    }

    private enum class ExitType {
        RETURN,
        BREAK,
        CONTINUE,
        INFINITE_LOOP,
        NONE
    }
}