package com.tobi.mc.computable.variable

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data

data class SetVariable(
    override var name: String,
    override var contextIndex: Int,
    var value: Computable,
    override var sourceRange: SourceRange? = null
) : Computable, VariableContext {

    override val description: String = "set variable"

    override fun getNodes(): Iterable<Computable> = listOf(value)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val data = value.compute(context, environment)
        context.setVariable(name, contextIndex, data, this)
        return data
    }
}