package com.tobi.mc.parser.ast.parser.runtime

internal class ComplexSymbolFactory : SymbolFactory {
    override fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?, value: Any?): Symbol {
        return ComplexSymbol(name, id, left, right, value)
    }

    override fun newSymbol(name: String, id: Int, left: Symbol?, right: Symbol?): Symbol {
        return ComplexSymbol(name, id, left, right, null)
    }

    override fun newSymbol(name: String, id: Int): Symbol {
        return ComplexSymbol(name, id, null as FileLocation?, null, null)
    }

    override fun startSymbol(name: String, id: Int, state: Int): Symbol {
        return ComplexSymbol(name, id, state)
    }
}