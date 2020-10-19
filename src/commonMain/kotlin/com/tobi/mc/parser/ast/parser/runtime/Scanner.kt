package com.tobi.mc.parser.ast.parser.runtime

interface Scanner {

    fun next_token(): Symbol?
}