package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.control.FlowInterrupt
import com.tobi.mc.computable.data.Data

class Program(var code: ExpressionSequence, var context: Context) : Computable {

    override val description: String = "program"

    suspend fun compute(executionEnvironment: ExecutionEnvironment) = compute(this.context, executionEnvironment)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return try {
            code.compute(context, environment)
        } catch (e: FlowInterrupt.Return) {
            e.toReturn
        } catch (e: FlowInterrupt.Continue) {
            throw ScriptException("Unexpected 'continue' outside of loop")
        } catch (e: FlowInterrupt.Break) {
            throw ScriptException("Unexpected 'break' outside of loop")
        }
    }
}