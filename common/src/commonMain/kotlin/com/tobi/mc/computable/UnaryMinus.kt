package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt

class UnaryMinus(var expression: DataComputable) : DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = expression.compute(context, environment)
        if(result !is DataTypeInt) {
            throw ScriptException("Expected int for negation, got ${result.description}")
        }
        return DataTypeInt(-result.value)
    }
}