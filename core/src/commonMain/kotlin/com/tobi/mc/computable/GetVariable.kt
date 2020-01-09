package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.ScriptException

class GetVariable(override val name: String) : VariableReference, DataComputable {

    override val components: Array<Computable> = emptyArray()

    override fun compute(context: Context, environment: ExecutionEnvironment): Data =
        context.getDataType(name) ?: throw ScriptException("Unknown variable '$name'")

    override fun optimise(): DataComputable = this
}