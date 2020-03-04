package com.tobi.mc.computable

import com.tobi.mc.computable.data.DataType

sealed class ComputableResult {

    abstract val description: String
}

abstract class Data : DataComputable, ComputableResult() {

    override val description: String
        get() = type.toString()

    abstract val type: DataType

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data = this

    abstract fun toScriptString(): String
}

abstract class FlowInterrupt : ComputableResult()