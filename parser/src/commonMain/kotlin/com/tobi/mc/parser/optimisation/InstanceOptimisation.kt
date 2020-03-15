package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

internal abstract class InstanceOptimisation<T : Computable>(private val `class`: KClass<T>) : Optimisation<T> {

    final override fun accepts(computable: Computable): Boolean = `class`.isInstance(computable)
}