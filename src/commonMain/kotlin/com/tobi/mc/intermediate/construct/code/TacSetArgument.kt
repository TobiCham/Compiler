package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.intermediate.TacStructure

data class TacSetArgument(
    val variable: TacVariableReference,
    val index: Int
) : TacStructure