package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.intermediate.TacStructure

class TacBranchEqualZero(
    val conditionVariable: TacVariableReference,
    val branchTo: String
) : TacStructure