package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacSetArgument(
    var variable: TacVariableReference,
    var index: Int
) : TacNode {

    override fun getNodes(): Iterable<TacNode> = listOf(variable)
}