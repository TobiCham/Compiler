package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacFunctionCall(var function: TacVariableReference) : TacNode {

    override fun getNodes(): Iterable<TacNode> = listOf(function)
}