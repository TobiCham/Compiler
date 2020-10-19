package com.tobi.mc

class ParseException(
    override val message: String,
    val source: SourceRange
) : RuntimeException(message) {

    constructor(message: String, sourceObject: SourceObject): this(
        message,
        sourceObject.sourceRange ?: throw IllegalArgumentException("Source object must have a range")
    )
}