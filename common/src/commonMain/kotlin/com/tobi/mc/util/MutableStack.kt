package com.tobi.mc.util

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

    /**
     * Removes all elements from the stack
     */
    fun clear()
}