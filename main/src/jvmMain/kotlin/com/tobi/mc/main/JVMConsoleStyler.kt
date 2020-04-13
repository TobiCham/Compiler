package com.tobi.mc.main

import com.tobi.mc.ProgramToStringStyler
import com.tobi.mc.StyleType

object JVMConsoleStyler : ProgramToStringStyler {

    private const val ANSI_RESET = "\u001B[0m"
    private const val ANSI_RED = "\u001B[31m"
    private const val ANSI_GREEN = "\u001B[32m"
    private const val ANSI_YELLOW = "\u001B[33m"
    private const val ANSI_BLUE = "\u001B[34m"
    private const val ANSI_PURPLE = "\u001B[35m"

    override fun style(type: StyleType, value: String): String {
        return "${getColor(type)}$value$ANSI_RESET"
    }

    private fun getColor(type: StyleType) = when(type) {
        StyleType.STRING -> ANSI_GREEN
        StyleType.INT -> ANSI_RED
        StyleType.TYPE_DECLARATION -> ANSI_PURPLE
        StyleType.IF, StyleType.ELSE, StyleType.WHILE -> ANSI_BLUE
        StyleType.CONTINUE, StyleType.BREAK, StyleType.RETURN -> ANSI_BLUE
        StyleType.BRACKET -> ""
        StyleType.CURLY_BRACKET -> ""
        StyleType.MATH -> ANSI_YELLOW
        StyleType.SEMI_COLON -> ""
        StyleType.NAME -> ""
        StyleType.ASSIGNMENT -> ""
        StyleType.PARAMS_SEPARATOR -> ""
        StyleType.NEGATION -> ANSI_YELLOW
        StyleType.STRING_CONCAT -> ANSI_YELLOW
    }
}