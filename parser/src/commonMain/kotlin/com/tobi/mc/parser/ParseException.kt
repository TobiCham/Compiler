package com.tobi.mc.parser

import com.tobi.mc.parser.ast.parser.runtime.FileLocation

class ParseException(message: String, val from: FileLocation?, val to: FileLocation?) : Exception(message) {

    fun createErrorMessage(originalSource: String): String {
        val index = if(to != null) findIndex(originalSource, to.line, to.column) else -1
        return buildString {
            append('\n')
            if(index >= 0) {
                append(originalSource.substring(0, index + 1))
                append('\n')

                for(i in 0 until to!!.column) append(' ')
            } else {
                append(originalSource)
            }
            append("^\n")
            append("Syntax Error - $message")
        }
    }

    private fun findIndex(source: String, line: Int, column: Int): Int {
        var lineCounter = 0
        var columnCounter = -1

        for((index, ch) in source.withIndex()) {
            if(ch == '\n') {
                lineCounter++
                columnCounter = 0
            } else {
                columnCounter++
            }
            if(lineCounter == line && columnCounter == column) {
                return index
            }
        }
        return -1
    }
}