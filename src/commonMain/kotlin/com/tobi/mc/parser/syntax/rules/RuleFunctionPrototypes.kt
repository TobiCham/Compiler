package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object RuleFunctionPrototypes : InstanceSyntaxRule<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Function prototypes implemented", """
        Ensures two criterion:
        1. All function prototypes have a definition at the same level as the prototype
        2. Implementations are created before the function is referenced
    """.trimIndent())

    override fun ExpressionSequence.validateInstance() {
        for ((i, operation) in this.operations.withIndex()) {
            if(operation is FunctionPrototype) {
                val implementation = findImplementation(this, i, operation)
                if(implementation == null) {
                    throw ParseException("No implementation found for prototype", operation)
                }
                if(!doSignaturesMatch(operation, implementation)) {
                    throw ParseException("Function prototype and definition cannot have different signatures", operation)
                }
                if(operation.returnType == null && implementation.returnType != null) {
                    operation.returnType = implementation.returnType
                } else if(operation.returnType != null && implementation.returnType == null) {
                    implementation.returnType = operation.returnType
                }
            }
        }
    }

    private fun doSignaturesMatch(prototype: FunctionPrototype, implementation: FunctionDeclaration): Boolean {
        if(prototype.parameters.size != implementation.parameters.size) {
            return false
        }
        for(i in 0 until prototype.parameters.size) {
            val p1 = prototype.parameters[i]
            val p2 = implementation.parameters[i]

            if(p1.name != p2.name || p1.type != p2.type) {
                return false
            }
        }
        if(prototype.returnType != null && implementation.returnType != null) {
            if(prototype.returnType != implementation.returnType) {
                return false
            }
        }
        return true
    }

    private fun findImplementation(sequence: ExpressionSequence, startIndex: Int, prototype: FunctionPrototype): FunctionDeclaration? {
        val ops = sequence.operations
        for(i in startIndex + 1 until ops.size) {
            val op = ops[i]
            if(op is FunctionDeclaration && op.name == prototype.name) {
                return op
            }
        }
        return null
    }
}