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

    override fun clear() {
        tail = null
        internalSize = 0
    }

    override fun get(index: Int): T {
        if(index < 0 || index >= size) {
            throw IndexOutOfBoundsException("$index out of range of 0...${size - 1}")
        }
        val actualIndex = size - index - 1
        var currentItem = tail
        for(i in 0 until actualIndex) {
            currentItem = currentItem!!.previous
        }
        return currentItem!!.item
    }

    private inner class LinkItem(val item: T, var previous: LinkItem?)

    private companion object {
        val EMPTY_EXCEPTION = NoSuchElementException("Stack is empty")
    }
}