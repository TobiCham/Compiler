package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

interface TacBranch : TacNode {

    val branches: List<TacBlock>
}