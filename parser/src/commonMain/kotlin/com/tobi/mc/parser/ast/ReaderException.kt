package com.tobi.mc.parser.ast

internal class ReaderException : Exception {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}