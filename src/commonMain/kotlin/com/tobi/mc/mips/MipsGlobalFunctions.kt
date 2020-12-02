package com.tobi.mc.mips

object MipsGlobalFunctions {

    val PRINT_INT = """
    printInt:
        lw ${'$'}a0, 0(${'$'}sp)
        li ${'$'}v0, 1
        syscall
        jr ${'$'}ra
    """.trimIndent()

    val PRINT_STRING = """
    printString:
        lw ${'$'}a0, 0(${'$'}sp)
        li ${'$'}v0, 4
        syscall
        jr ${'$'}ra
    """.trimIndent()
    
    val EXIT = """
    exit:
        lw ${'$'}a0, 0(${'$'}sp)
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
        li ${'$'}a0, 256
        li ${'$'}v0, 9
        syscall
        
        move ${'$'}a0, ${'$'}v0
        li ${'$'}a1, 256
        li ${'$'}v0, 8
        syscall
        move ${'$'}v0, ${'$'}a0
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