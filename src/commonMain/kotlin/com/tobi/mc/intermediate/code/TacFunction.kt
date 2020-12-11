package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.util.traverseAllNodes

class TacFunction(
    val environment: TacEnvironment,
    val variables: Set<String>,
    var code: TacBlock,
    val parameters: Int
) : TacExpression {

    override fun getNodes(): Iterable<TacNode> = listOf(code)

    fun calculateRegistersUsed(): Int {
        val registers = code.traverseAllNodes(filter = {
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