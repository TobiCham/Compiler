package com.tobi.mc.computable

object BreakStatement : Computable, FlowInterrupt() {

    override val description: String = "break"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult = this
}