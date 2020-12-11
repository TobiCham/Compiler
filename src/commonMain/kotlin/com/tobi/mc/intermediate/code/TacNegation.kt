package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

class TacNegation(var toNegate: TacVariableReference) : TacExpression {
    override fun getNodes(): Iterable<TacNode> = listOf(toNegate)
}