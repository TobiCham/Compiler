package com.tobi.mc.computable.function

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.computable.variable.VariableReference

data class FunctionDeclaration(
    override var name: String,
    var parameters: List<Parameter>,
    var body: ExpressionSequence,
    var returnType: DataType?,
    override var sourceRange: SourceRange? = null
) : VariableReference, Computable {

    override val description: String = "function declaration"

    override fun getNodes(): Iterable<Computable> = listOf(body)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeClosure {
        val closure = DataTypeClosure(parameters, context, body, returnType
            ?: throw ScriptException("Return type cannot be auto when executing", this))
        context.defineVariable(name, closure, this)
        return closure
    }
}