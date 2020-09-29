package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.intermediate.TacStructure
import kotlin.reflect.KClass

abstract class TacInstanceOptimisation<T : TacStructure>(private val `class`: KClass<T>) : TacOptimisation<T> {

    override fun accepts(tac: TacStructure): Boolean = `class`.isInstance(tac)
}