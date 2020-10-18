package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt

class Negation(var negation: Computable, override var sourceRange: SourceRange? = null) : Computable {

    override val description: String = "negation"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeInt {
        val checkResult = negation.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int for negation, got ${checkResult.description}", negation)
        }
        val value = checkResult.value
        return DataTypeInt(if(value == 0L) 1L else 0L)
    }
}