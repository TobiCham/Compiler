package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class IfStatement(var check: DataComputable, var ifBody: ExpressionSequence, var elseBody: ExpressionSequence?) : Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int in if, got ${checkResult.description}")
        }
        if(checkResult.value != 0) {
            return ifBody.compute(context, environment)
        }
        return elseBody?.compute(context, environment) ?: DataTypeVoid
    }
}