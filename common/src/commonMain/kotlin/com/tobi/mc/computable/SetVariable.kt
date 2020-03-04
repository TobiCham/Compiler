package com.tobi.mc.computable

class SetVariable(override var name: String, var value: DataComputable) : VariableReference, DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val data = value.compute(context, environment)
        context.setVariable(name, data)
        return data
    }
}