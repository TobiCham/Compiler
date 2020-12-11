package com.tobi.mc

import kotlin.reflect.KClass

abstract class InstanceOptimisation<T : Any, I>(private val kClass: KClass<I>) : Optimisation<T> where I : T {

    override fun optimise(item: T): OptimisationResult<T> = when {
        kClass.isInstance(item) -> (item as I).optimiseInstance()
        else -> noOptimisation()
    }

    abstract fun I.optimiseInstance(): OptimisationResult<T>
}