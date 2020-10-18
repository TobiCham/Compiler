package com.tobi.mc.main

import com.tobi.mc.ParseException
import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.intermediate.TacToString
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.MinusCParser
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

fun main() {
    val program = Files.readAllLines(File("examples/Higher or Lower.c").toPath()).joinToString("\n")
    val parser = MinusCParser()
    val ast = try {
        parser.parse(program)
    } catch (e: ParseException) {
        System.err.println(e.createDescriptiveErrorMessage(program))
        exitProcess(1)
    }
    runBlocking {
        ast.compute(JVMExecutionEnvironment)
    }
    val tac = TacGenerator(ast).toTac()
    println(TacToString.toString(tac))
    val mips = TacToMips(MipsConfiguration.StandardMips).toMips(tac)
    println(MipsAssemblyGenerator.generateAssembly(mips))
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
                    append(currLine.substring(source.end.column + 1))
                }
            }
            append('\n')
        }
        append(CharArray(if(source.start.line == source.end.line) source.start.column else (source.start.column + source.end.column) / 2) { ' ' }.concatToString())
        append('^')
        append('\n')
        append("Syntax Error at [${source.start.line}:${source.start.column} - ${source.end.line}:${source.end.column}]: $message")
    }
}