package com.tobi.mc.inbuilt

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.type.CompleteType
import com.tobi.mc.type.ExpandedType
import com.tobi.mc.type.FunctionType
import com.tobi.mc.type.KnownParameters

abstract class InbuiltFunction(
    name: String,
    val params: List<Pair<String, ExpandedType>>,
    val returnType: ExpandedType
) : InbuiltVariable(name, FunctionType(returnType, KnownParameters(params.map { it.second }))) {

    override fun computeData(context: Context): Data {
        val simpleParams = params.map { (name, type) ->
            Pair(name, (type as CompleteType).type)
        }
        return DataTypeClosure(simpleParams, context, ExpressionSequence(listOf(
            ReturnExpression(InbuiltFunctionComputable())
        )), (returnType as CompleteType).type)
    }

    abstract suspend fun compute(context: Context, environment: ExecutionEnvironment): Data

    private inner class InbuiltFunctionComputable : DataComputable {

        override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
            return this@InbuiltFunction.compute(context, environment)
        }
    }
}