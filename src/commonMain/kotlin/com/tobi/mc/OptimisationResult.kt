package com.tobi.mc

sealed class OptimisationResult<out T> {

    /**
     * No optimisation was made
     */
    class NoOptimisation<T> : OptimisationResult<T>()

    /**
     * Optimisations were made to the internal structure
     */
    class UpdateStructure<T> : OptimisationResult<T>()

    /**
     * Replaces this item with a new item
     * @param newItem The new item to use
     */
    class NewValue<T>(val newItem: T?) : OptimisationResult<T>()
}