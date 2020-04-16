package com.tobi.mc.parser.ast.parser.runtime

internal class Symbol(
    val name: String,
    val symbol: Int,
    var parseState: Int,
    var value: Any?,
    val locationLeft: FileLocation?,
    val locationRight: FileLocation?
) {

    constructor(name: String, symbol: Int): this(name, symbol, -1)

    constructor(name: String, symbol: Int, parseState: Int) : this(name, symbol, parseState, null, null, null)

    constructor(name: String, symbol: Int, left: Symbol?, right: Symbol?, value: Any?) : this(name, symbol, -1, value, left?.locationLeft, right?.locationRight)

    constructor(name: String, symbol: Int, left: Symbol?, right: Symbol?) : this(name, symbol, -1, null, left?.locationLeft, right?.locationRight)

    constructor(name: String, symbol: Int, locationLeft: FileLocation?, locationRight: FileLocation?, value: Any?): this(name, symbol, -1, value, locationLeft, locationRight)
}