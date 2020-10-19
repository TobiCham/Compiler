package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt

class UnaryMinus(var expression: Computable, override var sourceRange: SourceRange? = null) : Computable {

    override val description: String = "unary minus"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = expression.compute(context, environment)
        if(result !is DataTypeInt) {
            throw ScriptException("Expected int for unary minus, got ${result.description}", expression)
        }
        return DataTypeInt(-result.value)
    }
}