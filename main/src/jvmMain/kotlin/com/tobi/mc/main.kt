package com.tobi.mc

import com.tobi.mc.parser.ParserContext
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val parserContext = ParserContext.createContext()
    var ast = parserContext.parseFromString(Files.readAllLines(Path.of("program.c")).joinToString("\n"))

    ast = parserContext.processProgram(ast)

    println(ProgramToString.toString(ast))

//    val job = GlobalScope.launch {
//        ProgramRunner().run(ast, JVMExecutionEnvironment)
//    }
//    while(job.isActive) {
//        Thread.sleep(0L)
//    }
}