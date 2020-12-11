package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacInbuiltFunction(val label: String) : TacExpression {
    override fun getNodes(): Iterable<TacNode> = emptyList()
}