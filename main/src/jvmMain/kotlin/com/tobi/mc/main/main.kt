package com.tobi.mc.main

import com.tobi.mc.parser.ParserContext
import java.io.File
import java.nio.file.Files

fun main() {
    val parserContext = ParserContext.createContext()
    val program = Files.readAllLines(File("examples/Higher or Lower.c").toPath()).joinToString("\n")

    for(i in 0 until 100) parserContext.parseFromString(program)

    val t1 = System.currentTimeMillis()
    for(i in 0 until 25_000) {
        parserContext.parseFromString(program)
    }
    println(System.currentTimeMillis() - t1)

//    parserContext.validator.processProgram(ast)
//    parserContext.optimiser.processProgram(ast)
//    parserContext.typeDetection.processProgram(ast)
//    ContextIndexResolver.processProgram(ast)
//    parserContext.processProgram(ast)
//
//    println(TacGenerator(TabbedBuilder()).toTac(ast))
//    val tac = TacGenerator(ast).toTac()
//    println(TacToString.toString(tac))
//
//    println(ProgramToString(JVMConsoleStyler).toString(ast))
//
//    println(StringExtractor.extractStrings(ast))
//
//    println(ProgramToString(JVMConsoleStyler).toString(ast))
//
//    runBlocking {
//        ast.compute(DefaultContext(), JVMExecutionEnvironment)
//    }
}
