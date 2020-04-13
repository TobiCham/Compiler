package com.tobi.mc.computable

import com.tobi.mc.computable.data.DataTypeVoid

class ReturnExpression(var toReturn: DataComputable?): Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ReturnResult {
        val result = toReturn?.compute(context, environment) ?: DataTypeVoid
        return ReturnResult(result)
    }

    override fun toString(): String {
        return "return{$toReturn}"
    }
}