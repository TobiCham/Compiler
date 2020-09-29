package com.tobi.mc.mips

class MipsInstruction(val instruction: String, val args: List<MipsArgument>) {

    constructor(instruction: String, vararg args: MipsArgument): this(instruction, args.toList())

    class Builder(instruction: String) {

        val args: MutableList<MipsArgument> = ArrayList()
        var instruction: String = instruction
            private set

        fun instruction(instruction: String) = also {
            this.instruction = instruction
        }

        fun add(argument: MipsArgument) = also {
            this.args.add(argument)
        }

        fun addRegister(registerName: String) = also {
            args.add(MipsArgument.Register(registerName))
        }

        fun addIndirect(registerName: String, offset: Int = 0) = also {
            args.add(MipsArgument.IndirectRegister(registerName, offset))
        }

        fun addValue(value: Long) = also {
            args.add(MipsArgument.Value(value))
        }

        fun addLabel(label: String) = also {
            args.add(MipsArgument.Label(label))
        }

        fun build(): MipsInstruction = MipsInstruction(instruction, args)
    }
}