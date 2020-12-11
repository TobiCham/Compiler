package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

class TacUnaryMinus(var variable: TacVariableReference) : TacExpression {

    override fun getNodes(): Iterable<TacNode> = listOf(variable)
}