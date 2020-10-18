package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.updateComponentAtIndex

class ProgramOptimiser(
    val optimisations: List<Optimisation>
) {

    fun optimise(computable: Computable): Computable {
        var output = computable
        while(true) {
            output = optimiseTree(output) ?: break
        }
        return output
    }

    private fun optimiseTree(computable: Computable): Computable? {
        for((i, component) in computable.getComponents().withIndex()) {
            val optimisedValue = optimiseTree(component) ?: continue
            if(optimisedValue !== component) {
                computable.updateComponentAtIndex(i, optimisedValue)
            }
            return computable
        }
        for(optimisation in optimisations) {
            val oldSource = computable.sourceRange
            val newValue = optimisation.optimise(computable)
            if(newValue != null) {
                println(optimisation.description.name)
                if(newValue.sourceRange == null) {
                    newValue.sourceRange = oldSource
                }
                return newValue
            }
        }
        return null
    }
}