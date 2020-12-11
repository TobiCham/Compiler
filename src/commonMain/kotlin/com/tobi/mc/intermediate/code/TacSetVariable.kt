package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacSetVariable(
    var variable: TacMutableVariable,
    var value: TacExpression
) : TacNode {

    override fun getNodes(): Iterable<TacNode> = listOf(variable, value)
}