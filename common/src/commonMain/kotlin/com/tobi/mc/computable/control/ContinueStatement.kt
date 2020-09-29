package com.tobi.mc.computable.control

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment

object ContinueStatement : FlowInterruptComputable {

    override val description: String = "continue"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        throw FlowInterrupt.Continue
    }
}