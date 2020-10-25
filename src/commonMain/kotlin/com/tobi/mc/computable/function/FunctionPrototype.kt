package com.tobi.mc.computable.function

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.computable.variable.VariableReference

class FunctionPrototype(
    override var name: String,
    var parameters: List<Parameter>,
    var returnType: DataType?,
    override var sourceRange: SourceRange? = null
) : VariableReference, Computable {

    override val description: String = "function prototype"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return DataTypeVoid()
    }
}