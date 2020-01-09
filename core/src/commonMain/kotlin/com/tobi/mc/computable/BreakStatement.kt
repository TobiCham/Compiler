package com.tobi.mc.computable

import com.tobi.mc.ComputableResult
import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.FlowInterrupt

object BreakStatement : Computable, FlowInterrupt() {

    override val components: Array<Computable> = emptyArray()

    override val description: String = "break"

    override fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult = this

    override fun optimise(): Computable = this
}