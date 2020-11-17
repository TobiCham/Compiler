package com.tobi.mc.main

import com.tobi.mc.ParseException

fun main(args: Array<String>) {
    MinusCApplication().run(args)
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