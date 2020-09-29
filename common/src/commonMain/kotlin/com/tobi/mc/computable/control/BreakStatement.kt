package com.tobi.mc.computable.control

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment

object BreakStatement : FlowInterruptComputable {

    override val description: String = "break"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        throw FlowInterrupt.Break
    }
}