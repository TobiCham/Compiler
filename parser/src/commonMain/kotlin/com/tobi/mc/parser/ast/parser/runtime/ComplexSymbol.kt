package com.tobi.mc.parser.ast.parser.runtime

import com.tobi.mc.parser.ast.parser.ParserNode

internal class ComplexSymbol : Symbol {

    val name: String
    val locationLeft: FileLocation?
    val locationRight: FileLocation?

    constructor(name: String, symbol: Int, parseState: Int) : super(symbol, parseState) {
        this.name = name
        this.locationLeft = null
        this.locationRight = null
    }

    constructor(name: String, symbol: Int, left: Symbol?, right: Symbol?, value: Any?) : super(symbol, value) {
        this.name = name
        this.locationLeft = (left as? ComplexSymbol)?.locationLeft
        this.locationRight = (right as? ComplexSymbol)?.locationRight

        if(value is ParserNode) {
            value.leftLocation = locationLeft
            value.rightLocation = locationRight
        }
    }

    constructor(name: String, symbol: Int, locationLeft: FileLocation?, locationRight: FileLocation?, value: Any?): super(symbol, value) {
        this.name = name
        this.locationLeft = locationLeft
        this.locationRight = locationRight

        if(value is ParserNode) {
            value.leftLocation = locationLeft
            value.rightLocation = locationRight
        }
    }
}