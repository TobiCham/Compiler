package com.tobi.mc

import com.tobi.mc.analysis.ProgramToString
import com.tobi.mc.parser.NodeConverter
import com.tobi.mc.parser.syntax.types.TypeDetection
import java.io.File

fun main() {
    val wrapper = ParserWrapper(File("libs/parser-wrapper.jar"))
    val node = wrapper.parse(File("program.c"))

    val context = DefaultContext()
    val program = NodeConverter.convertProgram(node)
    TypeDetection.checkAndDetectTypes(program, context)

    println(ProgramToString.toString(program))
}