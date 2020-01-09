package com.tobi.mc.computable

import com.tobi.mc.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class WhileLoop(val check: DataComputable, val body: ExpressionSequence) : Computable {

    override val components: Array<Computable> = arrayOf(check, body)

    override fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        loop@ while(computeBoolean(context, environment)) {
            val result = body.compute(context, environment)
            if(result is FlowInterrupt) {
                when(result) {
                    is BreakStatement -> break@loop
                    is ContinueStatement -> continue@loop
                    else -> return result
                }
            }
        }
        return DataTypeVoid
    }

    private fun computeBoolean(context: Context, environment: ExecutionEnvironment): Boolean {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int, got ${checkResult.description}")
        }
        return checkResult.value != 0
    }

    override fun optimise(): Computable {
        val optimisedCheck = check.optimise()
        if(optimisedCheck is DataTypeInt && optimisedCheck.value == 0) {
            return ExpressionSequence(emptyList())
        }
        return WhileLoop(optimisedCheck, body.optimise())
    }
}