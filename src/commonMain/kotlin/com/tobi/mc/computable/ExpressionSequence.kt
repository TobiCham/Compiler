package com.tobi.mc.computable

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeVoid

class ExpressionSequence(var operations: List<Computable>, override var sourceRange: SourceRange? = null): Computable {

    override val description: String = "code block"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val newContext = Context(context)

        for(operation in operations) {
            operation.compute(newContext, environment)
        }
        return DataTypeVoid()
    }

    override fun toString(): String = "Block"
}