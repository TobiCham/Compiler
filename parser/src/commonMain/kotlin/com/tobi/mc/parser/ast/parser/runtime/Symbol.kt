package com.tobi.mc.parser.ast.parser.runtime

internal open class Symbol private constructor(
    val symbol: Int,
    var parseState: Int,
    var value: Any?
) {
    val left: Int = -1
    val right: Int = -1
    var used_by_parser: Boolean = false

    constructor(symbol: Int, parseState: Int) : this(symbol, parseState, null)
    constructor(symbol: Int, value: Any?) : this(symbol, -1, value)
}