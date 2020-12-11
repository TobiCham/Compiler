package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.code.TacBlock
import com.tobi.mc.intermediate.code.TacGoto
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object OptimisationBranchReduction : TacInstanceOptimisation<TacBlock>(TacBlock::class) {

    override val description: DescriptionMeta = SimpleDescription("Branch reduction", """
        Replaces blocks with a lead branch with the branching block
        E.g. (Goto -> Block1[Goto -> Block2]) -> (Goto -> Block2) 
    """.trimIndent())

    override fun TacBlock.optimiseInstance(): OptimisationResult<TacNode> {
        val first = this.instructions.firstOrNull() ?: return noOptimisation()
        if(first is TacGoto) {
            return newValue(first.block)
        }
        return noOptimisation()
    }
}