package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

abstract class InstanceOptimisation<T : Computable>(private val `class`: KClass<T>) : Optimisation<T> {

    final override fun accepts(computable: Computable): Boolean = `class`.isInstance(computable)
}

//internal inline fun <reified T : Computable> instanceOptimisation(description: SimpleDescription, crossinline optimise: T.((Computable) -> Boolean) -> Boolean): Optimisation<T> {
//    return object : Optimisation<T> {
//
//        override fun accepts(computable: Computable): Boolean = computable is T
//
//        override fun T.optimise(replace: (Computable) -> Boolean): Boolean {
//            return optimise.invoke(this, replace)
//        }
//
//        override val description: DescriptionMeta = description
//    }
//}