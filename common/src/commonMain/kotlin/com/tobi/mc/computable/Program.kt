package com.tobi.mc.computable

import com.tobi.mc.computable.data.DataTypeVoid

class Program(var code: ExpressionSequence, var context: DefaultContext) : Computable {

    suspend fun compute(executionEnvironment: ExecutionEnvironment) = compute(this.context, executionEnvironment)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        val result = code.compute(context, environment)
        if(result is ReturnResult) {
            return result.result
        }
        return DataTypeVoid
    }
}