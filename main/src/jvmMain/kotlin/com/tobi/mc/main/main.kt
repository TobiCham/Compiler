package com.tobi.mc.main

import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.intermediate.TacToString
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.ParserContext
import java.io.File
import java.nio.file.Files

fun main() {
    val parserContext = ParserContext.createContext()
    val program = Files.readAllLines(File("examples/Tac Test.c").toPath()).joinToString("\n")
    val ast = parserContext.parseFromString(program)
    parserContext.processProgram(ast)

//    runBlocking {
//        ast.compute(DefaultContext(), JVMExecutionEnvironment)
//    }

    val tac = TacGenerator(ast).toTac()
    println(TacToString.toString(tac))
    println()

    val mips = TacToMips(MipsConfiguration.StandardMips).toMips(tac)
    println(MipsAssemblyGenerator.generateAssembly(mips))

//    OptimisationRegisters.apply {
//        tac.functions[0].optimise()
//    }
//    exitProcess(0)
}