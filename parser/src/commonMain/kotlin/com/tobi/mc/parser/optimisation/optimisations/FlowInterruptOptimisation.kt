package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.FlowInterrupt
import com.tobi.mc.computable.ReturnExpression
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object FlowInterruptOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Flow Interruption", """
        When code is added to interrupt the flow of an expression sequence, all code below can be ignored. E.g:
        
        println("Hello");
        return;
        println("World");
        
        is mapped to:
        
        println("Hello");
        return;
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        val interruptIndex = this.operations.indexOfFirst {
            it is FlowInterrupt || it is ReturnExpression
        }
        if(interruptIndex < 0 || interruptIndex == this.operations.size - 1) {
            return false
        }
        val newSeq = ExpressionSequence(this.operations.slice(0 until interruptIndex + 1))
        return replace(newSeq)
    }
}