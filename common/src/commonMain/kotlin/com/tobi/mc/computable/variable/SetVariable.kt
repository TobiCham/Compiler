package com.tobi.mc.computable.variable

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data

class SetVariable(
    override var name: String,
    var contextIndex: Int,
    var value: Computable,
    override var sourceRange: SourceRange? = null
) : Computable, VariableReference {

    override val description: String = "set variable"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val data = value.compute(context, environment)
        context.setVariable(name, contextIndex, data, this)
        return data
    }
}