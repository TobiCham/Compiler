package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataType

class DefineVariable(override var name: String, var value: DataComputable, var expectedType: DataType?): VariableReference, DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val value = value.compute(context, environment)
        if(value is FlowInterrupt) {
            throw ScriptException("Can't define '$name' as '${value.description}'")
        }

        if(expectedType != null && value.type !== expectedType) {
            throw ScriptException("Failed to assign $name:$expectedType to ${value.type}")
        }
        context.defineVariable(name, value)
        return value
    }
}