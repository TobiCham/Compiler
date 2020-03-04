package com.tobi.mc.parser.ast.parser.runtime

import com.tobi.util.ArrayListStack
import com.tobi.util.MutableStack
import com.tobi.util.Stack

internal class VirtualParseStack(private val stack: Stack<Symbol>) {

    private var realNext: Int = 0
    private val virtualStack: MutableStack<Int> = ArrayListStack()

    init {
        getFromReal()
    }

    private fun getFromReal() {
        if (realNext >= stack.size) {
            return
        }
        val symbol = stack[stack.size - 1 - realNext]
        realNext++
        virtualStack.push(symbol.parseState)
    }

    fun top(): Int {
        return virtualStack.peek()
    }

    fun pop() {
        virtualStack.pop()
        if (virtualStack.isEmpty()) {
            getFromReal()
        }
    }

    fun push(state_num: Int) {
        virtualStack.push(state_num)
    }
}