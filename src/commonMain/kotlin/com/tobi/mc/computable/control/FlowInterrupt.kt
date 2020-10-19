package com.tobi.mc.computable.control

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt

sealed class FlowInterrupt : Throwable() {

    class Return(val toReturn: Data, val statement: ReturnStatement) : FlowInterrupt()
    class Break(val statement: BreakStatement) : FlowInterrupt()
    class Continue(val statement: ContinueStatement) : FlowInterrupt()
    class Exit(val code: DataTypeInt) : FlowInterrupt()
}

interface FlowInterruptComputable : Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing
}
