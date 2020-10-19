package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.util.DescriptionMeta

interface TacOptimisation<T : TacStructure> {

    val description: DescriptionMeta

    fun accepts(tac: TacStructure): Boolean

    /**
     * @return Whether a modification was made
     */
    fun T.optimise(): Boolean
}