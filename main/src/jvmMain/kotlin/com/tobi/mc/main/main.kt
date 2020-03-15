package com.tobi.mc.main

import com.tobi.mc.ProgramToString
import com.tobi.mc.parser.ParserContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files

fun main() {
    val parserContext = ParserContext.createContext()
    val text = Files.readAllLines(File("program.c").toPath()).joinToString("\n")

    val ast = parserContext.parseFromString(text)
    parserContext.processProgram(ast)

    println(ProgramToString(JVMConsoleStyler).toString(ast))

    val job = GlobalScope.launch {
        try {
            ast.compute(JVMExecutionEnvironment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    while(job.isActive) {
        Thread.sleep(0L)
    }
}