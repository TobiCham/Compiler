package com.tobi.mc.parser.ast.parser.runtime

internal interface Scanner {

    fun next_token(): Symbol?
}