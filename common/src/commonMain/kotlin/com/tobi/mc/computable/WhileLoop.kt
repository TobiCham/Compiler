package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class WhileLoop(var check: DataComputable, var body: ExpressionSequence) : Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        loop@ while(computeBoolean(context, environment)) {
            val result = body.compute(context, environment)
            if(result is FlowInterrupt) {
                when(result) {
                    is BreakStatement -> break@loop
                    is ContinueStatement -> continue@loop
                    else -> return result
                }
            }
        }
        return DataTypeVoid
    }

    private suspend fun computeBoolean(context: Context, environment: ExecutionEnvironment): Boolean {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int, got ${checkResult.description}")
        }
        return checkResult.value != 0L
    }
}