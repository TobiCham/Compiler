package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.computable.data.DataTypeVoid

class FunctionCall(var function: DataComputable, var arguments: List<DataComputable>) : DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val function = this.function.compute(context, environment)

        if(function !is DataTypeClosure) {
            throw ScriptException("Tried to invoke a function, got ${function.type}")
        }
        if(arguments.size > function.parameters.size) {
            throw ScriptException("Too many parameters specified for function. Expected ${function.parameters.size}, got ${arguments.size}")
        }
        if(arguments.size < function.parameters.size) {
            throw ScriptException("Too few parameters specified for function. Expected ${function.parameters.size}, got ${arguments.size}")
        }

        val newContext = Context(function.context)
        for((i, pair) in function.parameters.withIndex()) {
            val value = arguments[i].compute(context, environment)
            if(value.type != pair.second) {
                throw ScriptException("Invalid type on parameter index $i: Expected ${pair.second}, got ${value.type}")
            }
            newContext.defineVariable(pair.first, value)
        }

        val result = function.body.compute(newContext, environment)
        if(result is ReturnResult) {
            return result.result
        }
        if(function.returnType == DataType.VOID && result is DataTypeVoid) {
            return DataTypeVoid
        }
        throw ScriptException("Expected to return ${function.returnType}, got ${result.description}")
    }
}