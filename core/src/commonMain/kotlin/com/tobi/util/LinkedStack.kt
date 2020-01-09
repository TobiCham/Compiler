package com.tobi.util

class LinkedStack<T> : MutableStack<T> {

    private var internalSize = 0
    private var tail: LinkItem? = null

    override val size: Int
        get() = internalSize

    override fun isEmpty(): Boolean = size == 0

    override fun peek(): T {
        return (tail ?: throw EMPTY_EXCEPTION).item
    }

    override fun push(item: T) {
        tail = LinkItem(item, tail)
        internalSize++
    }

    override fun pop(): T {
        val currentTail = tail ?: throw EMPTY_EXCEPTION
        val toReturn = currentTail.item
        this.tail = currentTail.previous
        internalSize--
        return toReturn
    }

    private inner class LinkItem(val item: T, var previous: LinkItem?)

    private companion object {
        val EMPTY_EXCEPTION = NoSuchElementException("Stack is empty")
    }
}