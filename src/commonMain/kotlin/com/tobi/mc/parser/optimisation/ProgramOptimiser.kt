package com.tobi.mc.parser.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.util.updateNodeAtIndex
import com.tobi.mc.updateStructure

class ProgramOptimiser(
    val optimisations: List<ASTOptimisation>
) {

    constructor(vararg optimisations: ASTOptimisation): this(optimisations.toList())

    fun optimise(computable: Computable): Computable {
        if(optimisations.isEmpty()) {
            return computable
        }
        var output = computable
        while(true) {
            val result = optimiseTree(output)
            when(result) {
                is OptimisationResult.NoOptimisation -> break
                is OptimisationResult.NewValue -> output = result.newItem
                    ?: throw IllegalStateException("Returned null value")
            }
        }
        return output
    }

    private fun optimiseTree(computable: Computable): OptimisationResult<Computable> {
        for(optimisation in optimisations) {
            val oldSource = computable.sourceRange
            val result = optimisation.optimise(computable)

            when(result) {
                is OptimisationResult.UpdateStructure -> return result
                is OptimisationResult.NewValue -> {
                    if (result.newItem != null && result.newItem.sourceRange == null) {
                        //Update the source range if not set
                        result.newItem.sourceRange = oldSource
                    }
                    return result
                }
            }
        }
        for((i, node) in computable.getNodes().withIndex()) {
            val result = optimiseTree(node)
            when(result) {
                is OptimisationResult.NewValue -> {
                    if(result.newItem !== node) {
                        computable.updateNodeAtIndex(i, result.newItem)
                    }
                    return updateStructure()
                }
                is OptimisationResult.UpdateStructure -> return result
            }
        }
        return noOptimisation()
    }
}