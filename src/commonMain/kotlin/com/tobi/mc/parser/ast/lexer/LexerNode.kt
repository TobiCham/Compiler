package com.tobi.mc.parser.ast.lexer

import com.tobi.mc.FilePosition

data class LexerNode(
    val type: LexerNodeType,
    val textForm: String,
    val value: Any?,
    val line: Int,
    val column: Int
) {

    val startPosition by lazy {
        FilePosition(line, column)
    }

    val endPosition by lazy {
        val lines = textForm.split("\\R")
        if (lines.size == 1) {
            FilePosition(line, column + textForm.length)
        } else {
            FilePosition(line + lines.size - 1, lines[lines.size - 1].length)
        }
    }
}