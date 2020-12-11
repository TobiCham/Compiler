package com.tobi.mc.parser.optimisation

import com.tobi.mc.InstanceOptimisation
import com.tobi.mc.Optimisation
import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

interface ASTOptimisation : Optimisation<Computable>

abstract class ASTInstanceOptimisation<T : Computable>(kClass: KClass<T>) : InstanceOptimisation<Computable, T>(kClass), ASTOptimisation