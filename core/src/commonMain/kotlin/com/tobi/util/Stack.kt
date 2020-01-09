package com.tobi.util

interface Stack<out T> {

    /**
     * Returns the size of the collection.
     */
    val size: Int

    /**
     * Returns `true` if the collection is empty (contains no elements), `false` otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * @throws [NoSuchElementException] if the stack is empty
     * @return The top element on the stack
     */
    fun peek(): T
}

interface MutableStack<T> : Stack<T> {

    /**
     * Pushes an item onto the stack
     */
    fun push(item: T)

    /**
     * Removes and returns the top element on the stack
     * @throws [NoSuchElementException] if the stack is empty
     * @return The top element on the stack
     */
    fun pop(): T
}