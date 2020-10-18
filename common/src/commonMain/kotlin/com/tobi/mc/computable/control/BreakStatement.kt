package com.tobi.mc.computable.control

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment

class BreakStatement(override var sourceRange: SourceRange? = null) : FlowInterruptComputable {

    override val description: String = "break"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        throw FlowInterrupt.Break(this)
    }
}