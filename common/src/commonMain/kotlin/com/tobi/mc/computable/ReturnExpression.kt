package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeVoid

class ReturnExpression(var toReturn: Computable?): Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ReturnResult {
        val result = toReturn?.compute(context, environment) ?: DataTypeVoid
        if(result !is Data) {
            throw ScriptException("Cannot return '${result.description}'")
        }
        return ReturnResult(result)
    }
}