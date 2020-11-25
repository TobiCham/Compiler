package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.control.FlowInterrupt
import com.tobi.mc.computable.data.Data

data class Program(var code: ExpressionSequence, var context: Context, override var sourceRange: SourceRange? = null) : Computable {

    override val description: String = "program"

    suspend fun compute(executionEnvironment: ExecutionEnvironment) = compute(this.context, executionEnvironment)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return try {
            code.compute(context, environment)
        } catch (e: FlowInterrupt) {
            when(e) {
                is FlowInterrupt.Return -> e.toReturn
                is FlowInterrupt.Exit -> e.code
                is FlowInterrupt.Continue -> throw ScriptException("Unexpected 'continue' outside of loop", e.statement)
                is FlowInterrupt.Break -> throw ScriptException("Unexpected 'break' outside of loop", e.statement)
            }
        }
    }

    override fun toString(): String = "Program"
}