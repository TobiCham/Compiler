package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.analysis.ExpandedParameterList
import com.tobi.mc.analysis.ExpandedType
import com.tobi.mc.analysis.FunctionType
import com.tobi.mc.analysis.toParameterList
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DataComputable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.ReturnExpression
import com.tobi.mc.computable.data.DataTypeClosure

abstract class InbuiltFunction(
    name: String,
    val params: ExpandedParameterList,
    val returnType: ExpandedType
) : InbuiltVariable(name, FunctionType(returnType, params)) {

    override fun computeData(context: Context): Data {
        return DataTypeClosure(params.toParameterList(), context, ExpressionSequence(listOf(
            ReturnExpression(InbuiltFunctionComputable())
        )), returnType.type)
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