package com.tobi.mc.parser.ast.parser.runtime

import com.tobi.mc.FilePosition
import com.tobi.mc.SourceObject
import com.tobi.mc.SourceRange

class Symbol(
    val name: String,
    val symbol: Int,
    var parseState: Int,
    var value: Any?,
    val startPosition: FilePosition?,
    val endPosition: FilePosition?
) {

    init {
        if(value is SourceObject && startPosition != null && endPosition != null) {
            (value as SourceObject).sourceRange = SourceRange(startPosition, endPosition)
        }
    }

    constructor(name: String, symbol: Int): this(name, symbol, -1)

    constructor(name: String, symbol: Int, parseState: Int) : this(name, symbol, parseState, null, null, null)

    constructor(name: String, symbol: Int, left: Symbol?, right: Symbol?, value: Any?) : this(name, symbol, -1, value, left?.startPosition, right?.endPosition)

    constructor(name: String, symbol: Int, left: Symbol?, right: Symbol?) : this(name, symbol, -1, null, left?.startPosition, right?.endPosition)

    constructor(name: String, symbol: Int, positionLeft: FilePosition?, positionRight: FilePosition?, value: Any?): this(name, symbol, -1, value, positionLeft, positionRight)
}