package com.tobi.mc.parser.ast.lexer

object StringConverter {

    private val replacements = mapOf(
        'n' to '\n',
        't' to '\t',
        '"' to '"',
        '\\' to '\\',
        '\'' to '\''
    )

    fun convertToString(rawInput: String): String {
        if(rawInput.length < 2 || rawInput.first() != '"' || rawInput.last() != '"') {
            throw RuntimeException("Invalid string")
        }
        var currentStr = rawInput.substring(1, rawInput.length - 1)
        for((representation, replacement) in replacements) {
            currentStr = currentStr.replace("\\$representation", replacement.toString())
        }
        return currentStr
    }
}