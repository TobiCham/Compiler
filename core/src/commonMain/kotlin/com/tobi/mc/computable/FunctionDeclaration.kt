package com.tobi.mc.computable

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeClosure

class FunctionDeclaration(
    override val name: String,
    val parameters: ParameterList,
    val body: ExpressionSequence,
    var returnType: DataType?
) : VariableReference, DataComputable {

    override val components: Array<Computable> = arrayOf(body)

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeClosure {
        val closure = DataTypeClosure(parameters, context, body, returnType)
        context.defineVariable(name, closure)
        return closure
    }

    override fun optimise(): FunctionDeclaration {
        val optimisedBody = body.optimise()
        return FunctionDeclaration(name, parameters, optimisedBody, returnType)
    }
}