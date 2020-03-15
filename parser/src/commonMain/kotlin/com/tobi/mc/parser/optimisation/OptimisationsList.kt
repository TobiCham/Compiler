package com.tobi.mc.parser.optimisation

import com.tobi.mc.parser.optimisation.optimisations.*
import com.tobi.mc.parser.optimisation.optimisations.number.*

internal object OptimisationsList {
    val NUMBER_OPTIMISATIONS = arrayOf(
        NegationOptimisation,
        NegationOfEquals,
        AddZeroOptimisation,
        MultiplyByZeroOptimisation,
        MultiplyByOneOptimisation,
        ModOneOptimisation,
        TwoNumberOptimisation,
        RedundantVariablesOptimisation,
        NestedSequenceOptimisation,
        AssociativityOptimisation
    )

    val DEFAULT_OPTIMISATIONS = arrayOf(
        *NUMBER_OPTIMISATIONS,
        BranchOptimisation,
        FlowInterruptOptimisation,
        RedundantOperationOptimisation,
        ConstantReferenceOptimisation
    )
}