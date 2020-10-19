package com.tobi.mc.computable.control

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class IfStatement(
    var check: Computable,
    var ifBody: ExpressionSequence,
    var elseBody: ExpressionSequence?,
    override var sourceRange: SourceRange? = null
) : Computable {

    override val description: String = "if statement"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int in if, got ${checkResult.description}", check)
        }
        if(checkResult.value != 0L) {
            return ifBody.compute(context, environment)
        }
        return elseBody?.compute(context, environment) ?: DataTypeVoid()
    }
}