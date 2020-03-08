package com.tobi.mc

interface ProgramToStringStyler {
    fun style(type: StyleType, value: String): String
}

enum class StyleType {
    INT,
    STRING,
    TYPE_DECLARATION,
    IF,
    ELSE,
    WHILE,
    CONTINUE,
    BREAK,
    RETURN,
    BRACKET,
    CURLY_BRACKET,
    MATH,
    SEMI_COLON,
    NAME,
    ASSIGNMENT,
    PARAMS_SEPARATOR
}

object Stylers {
    val NONE = object : ProgramToStringStyler {
        override fun style(type: StyleType, value: String) = value
    }
}