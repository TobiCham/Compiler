package com.tobi.mc.mips

class MipsFunction(val name: String, val instructions: List<MipsInstruction>) {

    constructor(name: String, vararg instructions: MipsInstruction): this(name, instructions.toList())

    class Builder(name: String) {
        var name = name
            private set

        val instructions: MutableList<MipsInstruction> = ArrayList()

        fun name(name: String) = also {
            this.name = name
        }

        fun add(vararg instructions: MipsInstruction) = also {
            for (instruction in instructions) {
                this.instructions.add(instruction)
            }
        }

        fun build() = MipsFunction(name, instructions)
    }
}