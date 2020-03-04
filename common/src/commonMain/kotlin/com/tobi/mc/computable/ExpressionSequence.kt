package com.tobi.mc.computable

import com.tobi.mc.computable.data.DataTypeVoid

class ExpressionSequence(var operations: List<Computable>): Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        val newContext = Context(context)

        for(operation in operations) {
            val result = operation.compute(newContext, environment)
            if(result is FlowInterrupt) {
                return result
            }
        }
        return DataTypeVoid
    }
}