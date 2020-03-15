package com.tobi.mc.computable

import com.tobi.mc.ScriptException

class GetVariable(override var name: String) : VariableReference, DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data =
        context.getDataType(name) ?: throw ScriptException("Unknown variable '$name'")

    override fun toString(): String {
        return "Get($name)"
    }
}