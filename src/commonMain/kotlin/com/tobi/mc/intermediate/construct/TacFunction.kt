package com.tobi.mc.intermediate.construct

import com.tobi.mc.computable.data.DataType
import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.code.RegisterVariable
import com.tobi.mc.intermediate.util.asDeepSequence

class TacFunction(
    val environment: TacEnvironment,
    val variables: Map<String, DataType>,
    val code: List<TacStructure>,
    val parameters: Int
) : TacExpression {

    val registersUsed: Int by lazy(this::calculateRegistersUsed)

    private fun calculateRegistersUsed(): Int {
        val registers = code.asDeepSequence(filter = {
            it !is TacFunction
        }).filterIsInstance<RegisterVariable>()

        val register = registers.maxByOrNull {
            it.register
        } ?: return 0
        return register.register + 1
    }

    override fun toString(): String {
        return "TacFunction($variables=${variables} params=${parameters})"
    }
}