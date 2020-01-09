package com.tobi.mc

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DataComputable
import com.tobi.mc.computable.data.DataType

sealed class ComputableResult {

    abstract val description: String
}

abstract class Data : DataComputable, ComputableResult() {

    override val components: Array<Computable> = emptyArray()

    override val description: String
        get() = type.toString()

    abstract val type: DataType

    override fun compute(context: Context, environment: ExecutionEnvironment): Data = this

    override fun optimise(): DataComputable = this

    abstract fun toScriptString(): String
}

abstract class FlowInterrupt : ComputableResult()