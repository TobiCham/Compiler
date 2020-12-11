package com.tobi.mc.computable.control

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid

data class IfStatement(
    var check: Computable,
    var ifBody: ExpressionSequence,
    var elseBody: ExpressionSequence?,
    override var sourceRange: SourceRange? = null
) : Computable {

    override val description: String = "if statement"

    override fun getNodes(): Iterable<Computable> {
        val nodes = ArrayList<Computable>()
        nodes.add(check)
        nodes.add(ifBody)
        if(elseBody != null) {
            nodes.add(elseBody!!)
        }
        return nodes
    }

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val checkResult = check.compute(context, environment)
        if(checkResult !is DataTypeInt) {
            throw ScriptException("Expected int in if, got ${checkResult.description}", check)
        }
        if(checkResult.value != 0L) {
            return ifBody.compute(context, environment)
        }
        return elseBody?.compute(context, environment) ?: DataTypeVoid()
    }
}