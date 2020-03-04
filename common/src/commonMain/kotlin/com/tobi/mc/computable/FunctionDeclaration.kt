package com.tobi.mc.computable

import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeClosure

class FunctionDeclaration(
    override var name: String,
    var parameters: ParameterList,
    var body: ExpressionSequence,
    var returnType: DataType?
) : VariableReference, DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeClosure {
        val closure = DataTypeClosure(parameters, context, body, returnType)
        context.defineVariable(name, closure)
        return closure
    }
}