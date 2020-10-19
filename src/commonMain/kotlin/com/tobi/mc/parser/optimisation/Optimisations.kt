package com.tobi.mc.parser.optimisation

import com.tobi.mc.parser.optimisation.optimisations.*
import com.tobi.mc.parser.optimisation.optimisations.number.*

object Optimisations {

    val ALL_OPTIMISATIONS = listOf(
        UnaryMinusOptimisation,
        NegationOptimisation,
        NegationOfEquals,
        AddZeroOptimisation,
        MultiplyByZeroOptimisation,
        MultiplyByOneOptimisation,
        ModOneOptimisation,
        TwoNumberOptimisation,
        RedundantVariablesOptimisation,
        NestedSequenceOptimisation,
        AssociativityOptimisation,
        StringConcatOptimisation,

        BranchOptimisation,
        UnreachableCodeOptimisation,
        RedundantOperationOptimisation,
        ConstantReferenceOptimisation,
        TailRecursionOptimisation
    )
}