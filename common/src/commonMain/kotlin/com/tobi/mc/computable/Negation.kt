package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt

class Negation(var negation: DataComputable) : DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeInt {
        val checkResult = negation.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int for negation, got ${checkResult.description}")
        }
        val value = checkResult.value
        return DataTypeInt(if(value == 0L) 1L else 0L)
    }
}