package com.tobi.mc.main

import com.tobi.mc.ProgramToStringStyler
import com.tobi.mc.StyleType

object HtmlStyler : ProgramToStringStyler {

    private val replacements = mapOf(
        "&" to "&alt;",
        "<" to "&lt;",
        ">" to "&gt;",
        "\"" to "&quot;",
        "'" to "&#039;"
    )

    override fun style(type: StyleType, value: String): String {
        return "<span class='${getClassName(type)}'>${escapeHtml(
            value
        )}</span>"
    }

    private fun getClassName(type: StyleType) = "style-" + when(type) {
        StyleType.STRING -> "string"
        StyleType.INT -> "number"
        StyleType.TYPE_DECLARATION -> "type"
        StyleType.IF, StyleType.ELSE, StyleType.WHILE -> "control"
        StyleType.CONTINUE, StyleType.BREAK, StyleType.RETURN -> "flow"
        StyleType.BRACKET -> "bracket"
        StyleType.CURLY_BRACKET -> "curly-bracket"
        StyleType.MATH -> "math"
        StyleType.SEMI_COLON -> "semi-colon"
        StyleType.NAME -> "name"
        StyleType.ASSIGNMENT -> "assignment"
        StyleType.PARAMS_SEPARATOR -> "params"
        StyleType.NEGATION -> "math"
    }

    private fun escapeHtml(input: String) = replacements.entries.fold(input) { acc, (toReplace, replacement) ->
        acc.replace(toReplace, replacement)
    }
}

