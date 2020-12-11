package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.code.TacBlock
import com.tobi.mc.intermediate.util.updateNodeAtIndex
import com.tobi.mc.noOptimisation
import com.tobi.mc.updateStructure

class TacOptimiser(
    val optimisations: List<TacOptimisation>
) {

    constructor(vararg optimisations: TacOptimisation) : this(optimisations.toList())

    fun optimise(node: TacNode): TacNode {
        if(this.optimisations.isEmpty()) {
            return node
        }
        var output = node
        while(true) {
            val result = optimiseTree(output, HashSet())
            when(result) {
                is OptimisationResult.NoOptimisation -> break
                is OptimisationResult.NewValue -> output = result.newItem
                    ?: throw IllegalStateException("Returned null value")
            }
        }
        return output
    }

    private fun optimiseTree(tac: TacNode, encounteredBlocks: MutableSet<TacBlock>): OptimisationResult<TacNode> {
        if(tac is TacBlock && !encounteredBlocks.add(tac)) {
            return noOptimisation()
        }
        for((i, node) in tac.getNodes().withIndex()) {
            val result = optimiseTree(node, encounteredBlocks)
            when(result) {
                is OptimisationResult.NoOptimisation -> continue
                is OptimisationResult.NewValue -> {
                    if(result.newItem !== node) {
                        tac.updateNodeAtIndex(i, result.newItem)
                    }
                }
            }
            return updateStructure()
        }
        for(optimisation in optimisations) {
            val result = optimisation.optimise(tac)
            when(result) {
                is OptimisationResult.UpdateStructure -> return result
                is OptimisationResult.NewValue -> return result
            }
        }
        return noOptimisation()
    }
}