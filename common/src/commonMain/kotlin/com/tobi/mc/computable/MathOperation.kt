package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt

open class MathOperation(
    var arg1: DataComputable,
    var arg2: DataComputable,
    val operationString: String,
    val computation: (Long, Long) -> Long
) : DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = computation(getValue(arg1, context, environment), getValue(arg2, context, environment))
        return DataTypeInt(result)
    }

    private suspend fun getValue(computable: Computable, context: Context, environment: ExecutionEnvironment): Long {
        val value = computable.compute(context, environment)
        if(value !is DataTypeInt) {
            throw ScriptException("Expected int, got ${value.description}")
        }
        return value.value
    }

    override fun toString(): String {
        return "${this::class.simpleName}{$arg1, $arg2}"
    }
}
