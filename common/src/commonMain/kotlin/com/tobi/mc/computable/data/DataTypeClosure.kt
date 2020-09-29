package com.tobi.mc.computable.data

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.Invocable
import com.tobi.mc.computable.function.Parameter

class DataTypeClosure(
    override val parameters: List<Parameter>,
    val closure: Context,
    val body: ExpressionSequence,
    override val returnType: DataType
) : Invocable, Data() {

    override val type: DataType = DataType.FUNCTION

    override val description: String = "function<$returnType>(${parameters.joinToString(", ") { "${it.type} ${it.name}" }})"

    override suspend fun invoke(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        val newContext = Context(closure)
        for((i, parameter) in parameters.withIndex()) {
            newContext.defineVariable(parameter.name, arguments[i])
        }
        return body.compute(newContext, environment)
    }

    override fun toString(): String = description
}