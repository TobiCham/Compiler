package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt

open class MathOperation(
    val arg1: Computable,
    val arg2: Computable,
    val operationString: String,
    val computation: (Int, Int) -> Int
) : DataComputable {

    override val components: Array<Computable> = arrayOf(arg1, arg2)

    override fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val result = computation(getValue(arg1, context, environment), getValue(arg2, context, environment))
        return DataTypeInt(result)
    }

    private fun getValue(computable: Computable, context: Context, environment: ExecutionEnvironment): Int {
        val value = computable.compute(context, environment)
        if(value !is DataTypeInt) {
            throw ScriptException("Expected int, got ${value.description}")
        }
        return value.value
    }

    override fun optimise(): DataComputable {
        val newArg1 = arg1.optimise()
        val newArg2 = arg2.optimise()
        if(newArg1 is DataTypeInt && newArg2 is DataTypeInt) {
            return DataTypeInt(computation(newArg1.value, newArg2.value))
        }
        return MathOperation(newArg1, newArg2, operationString, computation)
    }
}
