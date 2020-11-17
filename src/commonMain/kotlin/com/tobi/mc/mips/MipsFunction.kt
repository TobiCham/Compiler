package com.tobi.mc.mips

data class MipsFunction(
    val label: String,
    var instructions: List<MipsInstruction>,
    val environmentVariables: Int,
    val parentEnvironments: Int
)