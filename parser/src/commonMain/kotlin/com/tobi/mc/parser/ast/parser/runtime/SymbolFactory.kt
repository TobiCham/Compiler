package com.tobi.mc.parser.ast.parser.runtime

internal interface SymbolFactory {

    fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?, value: Any?): Symbol
    fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?): Symbol

    fun newSymbol(name: String, id: Int): Symbol
    fun startSymbol(name: String, id: Int, state: Int): Symbol
}