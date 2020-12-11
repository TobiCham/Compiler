package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

object TacReturn : TacNode {
    override fun toString(): String = "ConstructReturn"

    override fun getNodes(): Iterable<TacNode> = emptyList()
}