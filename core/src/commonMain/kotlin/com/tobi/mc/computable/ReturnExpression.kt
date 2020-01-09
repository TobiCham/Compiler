package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeVoid

class ReturnExpression(val toReturn: Computable?): Computable {

    override val components: Array<Computable> = if(toReturn == null) emptyArray() else arrayOf(toReturn)

    override fun compute(context: Context, environment: ExecutionEnvironment): ReturnResult {
        val result = toReturn?.compute(context, environment) ?: DataTypeVoid
        if(result !is Data) {
            throw ScriptException("Cannot return '${result.description}'")
        }
        return ReturnResult(result)
    }

    override fun optimise() = ReturnExpression(toReturn?.optimise())
}