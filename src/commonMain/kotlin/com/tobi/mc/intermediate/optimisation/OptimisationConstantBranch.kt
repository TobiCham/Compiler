package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.code.IntValue
import com.tobi.mc.intermediate.code.TacBranchEqualZero
import com.tobi.mc.intermediate.code.TacGoto
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object OptimisationConstantBranch : TacInstanceOptimisation<TacBranchEqualZero>(TacBranchEqualZero::class) {

    override val description: DescriptionMeta = SimpleDescription("Constant Branch", """
        Optimises branches where the direction is known at compile time
    """.trimIndent())

    override fun TacBranchEqualZero.optimiseInstance(): OptimisationResult<TacNode> {
        val conditionVar = this.conditionVariable
        if(conditionVar is IntValue) {
            return newValue(TacGoto(if (conditionVar.value == 0L) this.successBlock else this.failBlock))
        }
        return noOptimisation()
    }
}