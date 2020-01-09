package com.tobi.mc.computable

import com.tobi.mc.*
import com.tobi.mc.computable.data.DataTypeVoid

class ExpressionSequence(val operations: List<Computable>): Computable {

    override val components: Array<Computable> = operations.toTypedArray()

    override fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult {
        val newContext = Context(context)

        for(operation in operations) {
            val result = operation.compute(newContext, environment)
            if(result is FlowInterrupt) {
                return result
            }
        }
        return DataTypeVoid
    }

    override fun optimise(): ExpressionSequence {
        val newOperations = ArrayList<Computable>()
        for (operation in this.operations) {
            val optimised = operation.optimise()
            if(isExpressionRequired(optimised)) {
                newOperations.add(optimised)

                //If a flow interrupt is encountered, nothing else afterwards matters
                if(optimised is FlowInterrupt) {
                    break
                }
            }
        }
        return ExpressionSequence(newOperations)
    }

    private fun isExpressionRequired(computable: Computable): Boolean = when {
        computable is ExpressionSequence && computable.operations.isEmpty() -> false
        computable is Data -> false
        else -> true
    }
}