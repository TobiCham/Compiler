package com.tobi.mc.parser.ast.parser

import com.tobi.mc.parser.ast.parser.runtime.FileLocation

internal data class ParserNode(val type: ParserNodeType, val left: ParserNode?, val right: ParserNode?, val value: Any?) {

    var leftLocation: FileLocation? = null
    var rightLocation: FileLocation? = null

    constructor(nodeType: ParserNodeType, left: ParserNode?, right: ParserNode?): this(nodeType, left, right, null)
    constructor(nodeType: ParserNodeType): this(nodeType, null, null)

    override fun toString(): String = buildString {
        append("Node(")

        val values = mapOf(
            "type" to type,
            "left" to left,
            "right" to right,
            "value" to value
        ).entries.filter { it.value != null }
        append(values.joinToString { (key, value) ->
            "$key=$value"
        })
        append(')')
    }
}