package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt

class UnaryMinus(var expression: Computable) : Computable {

    override val description: String = "unary minus"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = expression.compute(context, environment)
        if(result !is DataTypeInt) {
            throw ScriptException("Expected int for negation, got ${result.description}")
        }
        return DataTypeInt(-result.value)
    }
}