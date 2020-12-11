package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.code.TacBlock
import com.tobi.mc.intermediate.code.TacBranch
import com.tobi.mc.intermediate.code.TacReturn
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object OptimisationRedundantInstructions : TacInstanceOptimisation<TacBlock>(TacBlock::class) {

    override val description: DescriptionMeta = SimpleDescription("Redundant Instructions", """
        Removes any instructions in a block which proceed a branch
    """.trimIndent())

    override fun TacBlock.optimiseInstance(): OptimisationResult<TacNode> {
        var index = this.instructions.indexOfFirst { it is TacBranch || it is TacReturn }
        if(index < 0 || index == this.instructions.size - 1) {
            return noOptimisation()
        }
        return newValue(TacBlock(ArrayList(this.instructions.subList(0, index + 1))))
    }
}