package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.InstanceOptimisation
import com.tobi.mc.Optimisation
import com.tobi.mc.intermediate.TacNode
import kotlin.reflect.KClass

interface TacOptimisation : Optimisation<TacNode>
abstract class TacInstanceOptimisation<T : TacNode>(kClass: KClass<T>) : InstanceOptimisation<TacNode, T>(kClass), TacOptimisation