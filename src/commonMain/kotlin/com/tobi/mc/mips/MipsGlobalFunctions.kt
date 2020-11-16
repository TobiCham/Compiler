package com.tobi.mc.mips

object MipsGlobalFunctions {

    val PRINT_INT = """
    printInt:
        lw ${'$'}a0, 4(${'$'}sp)
        li ${'$'}v0, 1
        syscall
        jr ${'$'}ra
    """.trimIndent()

    val PRINT_STRING = """
    printString:
        lw ${'$'}a0, 4(${'$'}sp)
        li ${'$'}v0, 4
        syscall
        jr ${'$'}ra
    """.trimIndent()
    
    val EXIT = """
    exit:
        lw ${'$'}a0, 4(${'$'}sp)
        li ${'$'}v0, 17
        syscall
        jr ${'$'}ra
    """.trimIndent()

    val READ_INT = """
    readInt:
        li ${'$'}v0, 5
        syscall
        jr ${'$'}ra
    """.trimIndent()

    val READ_STRING = """
    readString:
        li ${'$'}v0, 4
        syscall
        jr ${'$'}ra
    """.trimIndent()

    val UNIX_TIME = """
    unixTime:
        li ${'$'}v0, 30
        syscall
        move ${'$'}v0, ${'$'}a0
        jr ${'$'}ra
    """.trimIndent()
}