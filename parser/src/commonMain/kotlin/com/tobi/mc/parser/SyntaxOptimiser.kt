package com.tobi.mc.parser

import com.tobi.mc.computable.Computable
import com.tobi.util.DescriptionMeta

interface SyntaxOptimiser : ParserOperation {

    /**
     * Optimises a computable, modifying the syntax tree
     * @param computable The computable to optimise
     * @return The optimised version - may be a new value or the existing value but optimised
     */
    fun optimise(computable: Computable): Computable

    /**
     * @return Details on the various optimisations used
     */
    val optimisationDescriptions: List<DescriptionMeta>
}