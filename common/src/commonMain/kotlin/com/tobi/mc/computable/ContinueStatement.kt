package com.tobi.mc.computable

object ContinueStatement : Computable, FlowInterrupt() {

    override val description: String = "continue"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult = this
}