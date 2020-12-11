package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

data class TacBranchEqualZero(
    var conditionVariable: TacVariableReference,
    var successBlock: TacBlock,
    var failBlock: TacBlock
) : TacNode, TacBranch {

    override val branches: List<TacBlock>
        get() = listOf(successBlock, failBlock)

    override fun getNodes(): Iterable<TacNode> = listOf(conditionVariable, successBlock, failBlock)
}