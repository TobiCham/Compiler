package com.tobi.mc.computable

import com.tobi.mc.ScriptException

class GetVariable(override var name: String, var contextIndex: Int) : VariableReference, DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return context.getDataType(name, contextIndex)?: throw ScriptException("Unknown variable '$name'")
    }

    override fun toString(): String {
        return "Get($name)"
    }
}