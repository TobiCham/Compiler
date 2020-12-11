package com.tobi.mc.computable.control

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeVoid

data class ReturnStatement(
    var toReturn: Computable?,
    override var sourceRange: SourceRange? = null
): FlowInterruptComputable {

    override val description: String = "return"

    override fun getNodes(): Iterable<Computable> = if(toReturn == null) emptyList() else listOf(toReturn!!)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Nothing {
        val result = toReturn?.compute(context, environment) ?: DataTypeVoid()
        throw FlowInterrupt.Return(result, this)
    }
}