package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import kotlin.reflect.KClass

abstract class InstanceSyntaxRule<T : Computable>(private val `class`: KClass<T>) : SyntaxRule {

    override fun validate(computable: Computable) {
        if(`class`.isInstance(computable)) {
            (computable as T).validateInstance()
        }
    }

    abstract fun T.validateInstance()
}