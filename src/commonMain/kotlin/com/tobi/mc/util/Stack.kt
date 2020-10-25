package com.tobi.mc.util

interface Stack<T> {

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

    /**
     * Returns the element in the stack at an index, from the bottom (i.e. those added first)
     * @throws [IndexOutOfBoundsException] if the index is out of bounds of 0 <= index < [size]
     * @return The element at the index
     */
    operator fun get(index: Int): T

    fun toList(): List<T>
}