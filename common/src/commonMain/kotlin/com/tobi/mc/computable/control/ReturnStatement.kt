package com.tobi.mc.computable.control

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeVoid

class ReturnStatement(var toReturn: Computable?): FlowInterruptComputable {

    override val description: String = "return"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        val result = toReturn?.compute(context, environment) ?: DataTypeVoid
        throw FlowInterrupt.Return(result)
    }
}