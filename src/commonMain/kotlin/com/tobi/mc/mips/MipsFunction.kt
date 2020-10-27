package com.tobi.mc.mips

class MipsFunction(
    val label: String,
    var instructions: List<MipsInstruction>,
    val stackVariables: Int,
    val registers: Int,
    val environmentVariables: Int
) {

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

        fun build(stackVariables: Int, registers: Int, environmentVariables: Int): MipsFunction {
            return MipsFunction(name, instructions, stackVariables, registers, environmentVariables)
        }
    }
}