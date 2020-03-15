package com.tobi.mc.util

class ArrayListStack<T> : MutableStack<T> {

    private val backingList = ArrayList<T>()

    override fun push(item: T) {
        backingList.add(item)
    }

    override fun pop(): T {
        if(isEmpty()) throw EMPTY_EXCEPTION
        return backingList.removeAt(size - 1)
    }

    override fun clear() {
        backingList.clear()
    }

    override val size: Int
        get() = backingList.size

    override fun isEmpty() = size == 0

    override fun peek(): T {
        if(isEmpty()) throw EMPTY_EXCEPTION
        return backingList.last()
    }

    override fun get(index: Int): T {
        return backingList[index]
    }

    private companion object {
        val EMPTY_EXCEPTION = NoSuchElementException("Stack is empty")
    }
}