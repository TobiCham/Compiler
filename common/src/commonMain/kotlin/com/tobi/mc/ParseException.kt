package com.tobi.mc

class ParseException : RuntimeException {

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}