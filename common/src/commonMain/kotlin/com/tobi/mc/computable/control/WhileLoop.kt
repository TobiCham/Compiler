package com.tobi.mc.computable.control

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class WhileLoop(var check: Computable, var body: ExpressionSequence) : Computable {

    override val description: String = "while loop"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        while(computeBoolean(context, environment)) {
            try {
                body.compute(context, environment)
            } catch (e: FlowInterrupt.Break) {
                break
            } catch (e: FlowInterrupt.Continue) {
                continue
            }
        }
        return DataTypeVoid
    }

    private suspend fun computeBoolean(context: Context, environment: ExecutionEnvironment): Boolean {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int, got ${checkResult.description}")
        }
        return checkResult.value != 0L
    }
}