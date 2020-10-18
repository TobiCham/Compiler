package com.tobi.mc

class ScriptException(
    override val message: String,
    val source: SourceObject?
) : RuntimeException(message)