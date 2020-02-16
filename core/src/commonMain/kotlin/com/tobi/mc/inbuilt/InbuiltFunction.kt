package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DataComputable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.ReturnExpression
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.parser.syntax.types.FunctionType
import com.tobi.mc.parser.syntax.types.KnownParameters
import com.tobi.mc.parser.syntax.types.ExpandedType
import com.tobi.mc.parser.syntax.types.CompleteType

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

    abstract fun compute(context: Context, environment: ExecutionEnvironment): Data

    private inner class InbuiltFunctionComputable : DataComputable {

        override fun compute(context: Context, environment: ExecutionEnvironment): Data {
            return this@InbuiltFunction.compute(context, environment)
        }

        override fun optimise(): DataComputable = this
        override val components: Array<Computable> = emptyArray()
    }
}