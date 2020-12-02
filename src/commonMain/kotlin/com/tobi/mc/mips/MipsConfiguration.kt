package com.tobi.mc.mips

interface MipsConfiguration {

    fun formatInstruction(instruction: MipsInstruction): String

    fun formatArgument(argument: MipsArgument): String

    val temporaryRegisters: Array<String>

    val argumentRegisters: Array<String>

    val zeroRegister: String

    val stackPointer: String

    val sysCallRegister: String

    val returnAddressRegister: String

    val closureRegister: String

    val resultRegister: String

    val wordSize: Int

    object StandardMips : MipsConfiguration {

        override fun formatInstruction(instruction: MipsInstruction): String = buildString {
            append(instruction.instruction)
            if(instruction.args.isNotEmpty()) {
                append(' ')
                append(instruction.args.map(this@StandardMips::formatArgument).joinToString(", "))
            }
        }

        override fun formatArgument(argument: MipsArgument): String = when(argument) {
            is Register -> "\$${argument.name}"
            is IndirectRegister -> "${argument.offset}(\$${argument.name})"
            is Label -> argument.label
            is Value -> argument.value.toString()
        }

        override val temporaryRegisters: Array<String> = arrayOf(
            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8", "t9",
            "k1", "fp",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
        )

        override val argumentRegisters: Array<String> = Array(4) {
            "a$it"
        }

        override val stackPointer: String = "sp"
        override val sysCallRegister: String = "v0"
        override val zeroRegister: String = "zero"
        override val returnAddressRegister: String = "ra"
        override val closureRegister: String = "k0"
        override val resultRegister: String = "v0"

        override val wordSize: Int = 4
    }
}