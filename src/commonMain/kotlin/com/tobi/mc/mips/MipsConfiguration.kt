package com.tobi.mc.mips

interface MipsConfiguration {

    fun formatInstruction(instruction: MipsInstruction): String

    fun formatArgument(argument: MipsArgument): String

    fun getSysCallCode(type: SysCallType): Int

    val temporaryRegisters: Array<String>

    val argumentRegisters: Array<String>

    val zeroRegister: String

    val stackPointer: String

    val framePointer: String

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
            "t0", "t1", "t2", "t3", "t4", "t5",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7"
        )

        override val argumentRegisters: Array<String> = Array(4) {
            "a$it"
        }

        override fun getSysCallCode(type: SysCallType) = when(type) {
            SysCallType.PRINT_INT -> 1
            SysCallType.PRINT_STRING -> 4
            SysCallType.READ_INT -> 5
            SysCallType.READ_STRING -> 8
            SysCallType.ALLOCATE_HEAP -> 9
            SysCallType.EXIT -> 17
        }

        override val stackPointer: String = "sp"
        override val framePointer: String = "fp"
        override val sysCallRegister: String = "v0"
        override val zeroRegister: String = "zero"
        override val returnAddressRegister: String = "ra"
        override val closureRegister: String = "k0"
        override val resultRegister: String = "v0"

        override val wordSize: Int = 4
    }
}