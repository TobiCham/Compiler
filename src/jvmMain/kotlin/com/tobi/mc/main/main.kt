package com.tobi.mc.main

import com.tobi.mc.ParseException
import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.MinusCParser
import com.tobi.mc.parser.ParserConfiguration
import kotlin.system.exitProcess


fun main() {
//    val program = Files.readAllLines(File("examples/Higher or Lower.c").toPath()).joinToString("\n")
    val program = """
        int x = 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1 * 1;
    """.trimIndent()
    val parser = MinusCParser(ParserConfiguration(optimisations = emptyList()))
    val ast = try {
        parser.parse(program)
    } catch (e: ParseException) {
        System.err.println(e.createDescriptiveErrorMessage(program))
        exitProcess(1)
    }
//    println(ProgramToString(JVMConsoleStyler).toString(ast))

//    runBlocking {
//        ast.compute(JVMExecutionEnvironment)
//    }

    val tac = TacGenerator.toTac(ast)
//    println(TacToString.toString(tac))

//    runBlocking {
//        TacEmulator.emulate(tac, JVMExecutionEnvironment)
//    }

    val mips = TacToMips(MipsConfiguration.StandardMips).toMips(tac)
    val result = MipsAssemblyGenerator.generateAssembly(mips)
    println(result)
}

fun ParseException.createDescriptiveErrorMessage(originalSource: String): String {
    val lines = originalSource.split(Regex("\\R"))

    return buildString {
        append(JVMConsoleStyler.ANSI_RED)
        for(i in source.start.line until source.end.line + 1) {
            val currLine = lines[i]
            var textStart = 0
            var textEnd = currLine.length
            if(i == source.start.line) {
                append(currLine.substring(0, source.start.column))
                append(JVMConsoleStyler.ANSI_UNDERLINE)
                textStart = source.start.column
            }
            if(i == source.end.line) textEnd = source.end.column
            append(currLine.substring(textStart, textEnd))
            if(i == source.end.line) {
                append(JVMConsoleStyler.ANSI_CLEAR_UNDERLINE)

                if(source.end.column < currLine.length) {
                    append(currLine.substring(source.end.column))
                }
            }
            append('\n')
        }
        val length = if(source.start.line == source.end.line) source.start.column else (source.start.column + source.end.column) / 2
        append(CharArray(length) { ' ' })
        append('^')
        append('\n')
        append("Syntax Error at [${source.start.line}:${source.start.column} - ${source.end.line}:${source.end.column}]: $message")
    }
}