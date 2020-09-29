package com.tobi.mc.computable.variable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data

class GetVariable(override var name: String, var contextIndex: Int) : VariableReference, Computable {

    override val description: String = "get variable"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return context.getDataType(name, contextIndex)?: throw ScriptException("Unknown variable '$name'")
    }

    override fun toString(): String {
        return "Get($name)"
    }
}