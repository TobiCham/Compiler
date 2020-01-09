package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment

class SetVariable(override val name: String, val value: DataComputable) : VariableReference, DataComputable {

    override val components: Array<Computable> = arrayOf(value)

    override fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val data = value.compute(context, environment)
        context.setVariable(name, data)
        return data
    }

    override fun optimise() = SetVariable(name, value.optimise())
}