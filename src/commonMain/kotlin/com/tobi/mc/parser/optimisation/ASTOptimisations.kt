package com.tobi.mc.parser.optimisation

import com.tobi.mc.parser.optimisation.optimisations.*
import com.tobi.mc.parser.optimisation.optimisations.number.*

object ASTOptimisations {

    val ALL_OPTIMISATIONS = listOf(
        UnaryMinusOptimisation,
        NegationOptimisation,
        NegationOfEquals,
        AddZeroOptimisation,
        MultiplyByOneOptimisation,
        ConstantMathOptimisation,
        RedundantVariablesOptimisation,
        NestedSequenceOptimisation,
        AssociativityOptimisation,
        StringConcatOptimisation,

        ConstantBranchOptimisation,
        UnreachableCodeOptimisation,
        RedundantOperationOptimisation,
        ConstantReferenceOptimisation,
        TailRecursionOptimisation,
        SingleFunctionCallOptimisation
    )
}