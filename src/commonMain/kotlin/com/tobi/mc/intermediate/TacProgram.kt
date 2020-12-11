package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.TacFunction

class TacProgram(
    val strings: Array<String>,
    var mainFunction: TacFunction
) : TacNode {

    override fun getNodes(): Iterable<TacNode> = listOf(mainFunction)
}