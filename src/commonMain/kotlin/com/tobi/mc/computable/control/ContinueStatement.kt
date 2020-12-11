package com.tobi.mc.computable.control

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment

data class ContinueStatement(override var sourceRange: SourceRange? = null) : FlowInterruptComputable {

    override val description: String = "continue"

    override fun getNodes(): Iterable<Computable> = emptyList()

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        throw FlowInterrupt.Continue(this)
    }
}