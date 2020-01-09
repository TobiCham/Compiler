package com.tobi.mc.computable

import com.tobi.mc.ComputableResult
import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

class IfStatement(val check: DataComputable, val ifBody: ExpressionSequence, val elseBody: ExpressionSequence?) : Computable {

    override val components: Array<Computable> = if(elseBody == null) arrayOf(check, ifBody) else arrayOf(check, ifBody, elseBody)

    override fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int in if, got ${checkResult.description}")
        }
        if(checkResult.value != 0) {
            return ifBody.compute(context, environment)
        }
        return elseBody?.compute(context, environment) ?: DataTypeVoid
    }

    override fun optimise(): Computable {
        val newCheck = check.optimise()
        if(newCheck is DataTypeInt && newCheck.value != 0) {
            //Check is always true
            return ifBody.optimise()
        }
        return IfStatement(newCheck, ifBody.optimise(), elseBody?.optimise())
    }
}