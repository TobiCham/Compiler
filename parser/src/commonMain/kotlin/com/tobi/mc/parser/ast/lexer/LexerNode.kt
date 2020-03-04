package com.tobi.mc.parser.ast.lexer

internal data class LexerNode(val type: LexerNodeType, val value: Any?, val line: Int, val column: Int)