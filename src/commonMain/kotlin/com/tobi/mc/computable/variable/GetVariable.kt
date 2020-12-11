package com.tobi.mc.computable.variable

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data

data class GetVariable(
    override var name: String,
    override var contextIndex: Int,
    override var sourceRange: SourceRange? = null
) : VariableContext, Computable {

    override val description: String = "get variable"

    override fun getNodes(): Iterable<Computable> = emptyList()

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return context.getDataType(name, contextIndex)?: throw ScriptException("Unknown variable '$name'", this)
    }

    override fun toString(): String {
        return "Get<$name,$contextIndex>"
    }
}