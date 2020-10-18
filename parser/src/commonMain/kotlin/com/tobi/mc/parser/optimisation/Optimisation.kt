package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import com.tobi.mc.util.DescriptionMeta

interface Optimisation {

    val description: DescriptionMeta

    /**
     * @return The new value to replace in the tree with, or null if no changes have been made
     */
    fun optimise(computable: Computable): Computable?
}