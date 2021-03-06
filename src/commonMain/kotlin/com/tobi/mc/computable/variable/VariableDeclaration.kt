package com.tobi.mc.computable.variable

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataType

data class VariableDeclaration(
    override var name: String,
    var value: Computable,
    var expectedType: DataType?,
    override var sourceRange: SourceRange? = null
): VariableReference, Computable {

    override val description: String = "variable declaration"

    override fun getNodes(): Iterable<Computable> = listOf(value)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val value = value.compute(context, environment)

        if(expectedType != null && value.type !== expectedType) {
            throw ScriptException("Failed to assign $name:$expectedType to ${value.type}", this)
        }
        context.defineVariable(name, value, this)
        return value
    }

    override fun toString(): String = "Define<$name>($value)"
}