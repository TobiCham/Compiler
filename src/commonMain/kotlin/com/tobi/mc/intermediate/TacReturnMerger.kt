package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.TacBlock
import com.tobi.mc.intermediate.code.TacFunction
import com.tobi.mc.intermediate.code.TacGoto
import com.tobi.mc.intermediate.code.TacReturn

object TacReturnMerger {

    fun mergeReturns(node: TacNode) {
        return node.mergeReturns(TacBlock(arrayListOf(TacReturn)), HashSet())
    }

    private fun TacNode.mergeReturns(returnBlock: TacBlock, encounteredBlocks: MutableSet<TacBlock>) {
        if(this is TacFunction) {
            return this.code.mergeReturns(TacBlock(arrayListOf(TacReturn)), HashSet())
        }
        if(this is TacBlock) {
            if(this === returnBlock || !encounteredBlocks.add(this)) {
                return
            }
            for((i, instruction) in this.instructions.withIndex()) {
                if(instruction is TacReturn) {
                    this.instructions[i] = TacGoto(returnBlock)
                }
            }
        }
    }
}