package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacGoto(var block: TacBlock) : TacNode, TacBranch {

    override val branches: List<TacBlock>
        get() = listOf(block)

    override fun getNodes(): Iterable<TacNode> = listOf(block)
}