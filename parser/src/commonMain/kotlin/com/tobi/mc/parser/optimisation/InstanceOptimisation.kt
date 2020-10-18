package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

abstract class InstanceOptimisation<T : Computable>(private val `class`: KClass<T>) : Optimisation {

    override fun optimise(computable: Computable): Computable? = when {
        `class`.isInstance(computable) -> (computable as T).optimiseInstance()
        else -> null
    }

    abstract fun T.optimiseInstance(): Computable?
}