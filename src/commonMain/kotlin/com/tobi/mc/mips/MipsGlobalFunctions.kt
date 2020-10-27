package com.tobi.mc.mips

object MipsGlobalFunctions {

    const val PRINT_INT = """
        lw ${'$'}a0, (${'$'}sp)
        li ${'$'}v0, 1
        syscall
        b removeClosureFrame
    """

    const val PRINT_STRING = """
        lw ${'$'}a0, (${'$'}sp)
        li ${'$'}v0, 4
        syscall
        b removeClosureFrame
    """
    
    const val EXIT = """
        lw ${'$'}a0, (${'$'}sp)
        li ${'$'}v0, 17
        syscall
        b removeClosureFrame
    """

    const val READ_INT = """
        li ${'$'}v0, 5
        syscall
        move ${'$'}t6, ${'$'}v0
        b removeClosureFrame
    """

    const val READ_STRING = """
        li ${'$'}v0, 4
        syscall
        move ${'$'}t6, ${'$'}v0
        b removeClosureFrame
    """

    const val UNIX_TIME = """
        li ${'$'}v0, 30
        syscall
        move ${'$'}t6, ${'$'}v0
        b removeClosureFrame
    """
}