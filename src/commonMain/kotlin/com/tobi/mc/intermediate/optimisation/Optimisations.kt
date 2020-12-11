package com.tobi.mc.intermediate.optimisation

object Optimisations {

    val ALL_OPTIMISATIONS = listOf(
        OptimisationBranchReduction,
        OptimisationConstantBranch,
        OptimisationRedundantInstructions,
        OptimisationRegisters
    )
}