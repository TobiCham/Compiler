package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import com.tobi.mc.util.DescriptionMeta

internal interface Optimisation<T : Computable> {

    val description: DescriptionMeta

    fun accepts(computable: Computable): Boolean

    /**
     * @param replace Tells the optimiser to replace this branch in the tree with the given value. Returns true
     * @return Whether a modification was made
     */
    fun T.optimise(replace: (Computable) -> Boolean): Boolean
}