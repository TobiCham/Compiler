package com.tobi.mc.intermediate

import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

class RegisterUse {

    private val inUse: MutableSet<Int> = HashSet()
    private val operations: MutableStack<MutableSet<Int>> = ArrayListStack()

    init {
        beginOperation()
    }

    fun findAvailable(): Int {
        if(operations.isEmpty()) {
            throw IllegalStateException("Must be in an operation")
        }
        val currentOp = operations.peek()
        tailrec fun findFree(start: Int): Int =
            if(!inUse.contains(start)) start else findFree(start + 1)
        return findFree(0).apply {
            inUse.add(this)
            currentOp.add(this)
        }
    }

    fun beginOperation() {
        val indices = HashSet<Int>()
        operations.push(indices)
    }

    fun endOperation() {
        val operation = operations.pop()
        inUse.removeAll(operation)
    }
}