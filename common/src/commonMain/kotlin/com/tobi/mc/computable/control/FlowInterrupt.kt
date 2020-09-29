package com.tobi.mc.computable.control

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data

sealed class FlowInterrupt : Throwable() {

    class Return(val toReturn: Data) : FlowInterrupt()
    object Break : FlowInterrupt()
    object Continue : FlowInterrupt()
}

interface FlowInterruptComputable : Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing
}
