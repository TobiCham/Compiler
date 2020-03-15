package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

internal abstract class InstanceSyntaxRule<T : Computable>(private val `class`: KClass<T>) : SyntaxRule<T> {

    final override fun accepts(computable: Computable): Boolean = `class`.isInstance(computable)
}