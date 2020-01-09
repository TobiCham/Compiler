package com.tobi.mc.computable

import com.tobi.mc.*
import com.tobi.mc.computable.data.DataType

class DefineVariable(override val name: String, val value: DataComputable, expectedType: DataType?): VariableReference, DataComputable {

    var expectedType: DataType? = expectedType
        private set

    override val components: Array<Computable> = arrayOf(value)

    override fun compute(context: Context, environment: ExecutionEnvironment): Data {
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

    fun setExpectedType(type: DataType) {
        this.expectedType = type
    }

    override fun optimise() = DefineVariable(name, value.optimise(), expectedType)
}