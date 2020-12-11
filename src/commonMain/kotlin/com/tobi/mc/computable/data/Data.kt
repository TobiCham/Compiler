package com.tobi.mc.computable.data

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment

abstract class Data : Computable {

    override val description: String
        get() = type.toString()

    abstract val type: DataType

    override fun getNodes(): Iterable<Computable> = emptyList()

    final override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data = this
}