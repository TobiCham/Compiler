package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

class TacStringConcat(var str1: TacVariableReference, var str2: TacVariableReference) : TacExpression {

    override fun getNodes(): Iterable<TacNode> = listOf(str1, str2)
}