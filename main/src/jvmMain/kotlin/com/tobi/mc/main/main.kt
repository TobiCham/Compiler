package com.tobi.mc.main

import com.tobi.mc.computable.DefaultContext
import com.tobi.mc.parser.ParserContext
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files

fun main() {
    val parserContext = ParserContext.createContext()
    val text = Files.readAllLines(File("program.c").toPath()).joinToString("\n")
    val ast = parserContext.parseFromString(text)

    parserContext.processProgram(ast)

    runBlocking {
        ast.compute(DefaultContext(), JVMExecutionEnvironment)
    }
}
