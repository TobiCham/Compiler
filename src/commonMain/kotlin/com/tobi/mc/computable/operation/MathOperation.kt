package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt

abstract class MathOperation(
    val operationString: String,
    val computation: (Long, Long) -> Long,
) : Computable {

    abstract var arg1: Computable
    abstract var arg2: Computable

    override val description: String = operationString

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = computation(getValue(arg1, context, environment), getValue(arg2, context, environment))
        return DataTypeInt(result)
    }

    private suspend fun getValue(computable: Computable, context: Context, environment: ExecutionEnvironment): Long {
        val value = computable.compute(context, environment)
        if(value !is DataTypeInt) {
            throw ScriptException("Expected int, got ${value.description}", this)
        }
        return value.value
    }

    override fun toString(): String {
        return "${this::class.simpleName}<$arg1, $arg2>"
    }
}
