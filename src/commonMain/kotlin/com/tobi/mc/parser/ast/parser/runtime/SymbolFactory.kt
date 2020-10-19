package com.tobi.mc.parser.ast.parser.runtime

class SymbolFactory {

    fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?, value: Any?) = Symbol(name, id, left, right, value)
    fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?) = Symbol(name, id, left, right)

    fun newSymbol(name: String, id: Int) = Symbol(name, id)
    fun startSymbol(name: String, id: Int, state: Int) = Symbol(name, id, state)
}