package com.tobi.mc.mips

class MipsInstruction(val instruction: String, val args: List<MipsArgument>) {

    constructor(instruction: String, vararg args: MipsArgument): this(instruction, args.toList())
}