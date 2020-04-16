package com.tobi.mc.parser

import com.tobi.mc.parser.ast.parser.runtime.FileLocation

class ParseException(message: String, val from: FileLocation?, val to: FileLocation?) : Exception(message) {

    fun createErrorMessage(originalSource: String): String {
        val message = if(to == null) {
            val lines = originalSource.split('\n')
            ErrorResult(originalSource, lines.last().length)
        } else getError(originalSource, to.line, to.column)

        return buildString {
            append('\n')
            append(message.stringToDisplay)
            append('\n')

            append(String(CharArray(message.errorColumn) { ' ' }))
            append('^')
            append('\n')

            append("Syntax Error - ${super.message}")
        }
    }

    private fun getError(source: String, line: Int, column: Int): ErrorResult {
        val lines = source.split('\n')
        val text = if(line >= lines.size) source else lines.subList(0, line + 1).joinToString("\n")
        return ErrorResult(text, column)
    }

    companion object {
        private data class ErrorResult(val stringToDisplay: String, val errorColumn: Int)
    }
}