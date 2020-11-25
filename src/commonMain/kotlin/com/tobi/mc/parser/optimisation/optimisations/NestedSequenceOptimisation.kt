package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object NestedSequenceOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Nested sequence", """
        It can occur that expression sequences are nested, e.g. when the body of an if will always succeed.
        This will split those sequences out
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val newOps = ArrayList<Computable>(this.operations.size)

        for(operation in this.operations) {
            if(operation is ExpressionSequence) {
                val usedVariables = this.findUnavailableVariables()
                operation.renameVariables(usedVariables)
                operation.decrementVariableContexts()
                newOps.addAll(operation.operations)
            } else {
                newOps.add(operation)
            }
        }
        if(newOps.size != this.operations.size) {
            return ExpressionSequence(newOps)
        }
        return null
    }

    private fun ExpressionSequence.renameVariables(usedVariables: MutableSet<String>) {
        for(operation in this.operations) {
            if(operation is FunctionDeclaration || operation is VariableDeclaration) {
                if(usedVariables.contains((operation as VariableReference).name)) {
                    val newName = findNewName(usedVariables, operation.name)
                    this.renameVariable(operation.name, newName)
                    operation.name = newName
                    usedVariables.add(newName)
                }
            }
        }
    }

    private fun ExpressionSequence.renameVariable(oldName: String, newName: String) {
        this.traverseWithDepth().filter { (computable, depth) ->
            computable is VariableContext && computable.name == oldName && computable.contextIndex == depth - 1
        }.forEach { (variable, _) ->
            (variable as VariableContext).name = newName
        }
    }

    private fun ExpressionSequence.decrementVariableContexts() {
        for((component, depth) in this.traverseWithDepth()) {
            if(component is VariableContext && component.contextIndex >= depth) {
                component.contextIndex--
            }
        }
    }

    private fun ExpressionSequence.findUnavailableVariables(): MutableSet<String> = this.traverseWithDepth().mapNotNull { (computable, depth) ->
        if(depth == 1 && (computable is FunctionDeclaration || computable is VariableDeclaration)) {
            (computable as VariableReference).name
        } else if(computable is VariableContext && computable.contextIndex >= depth) {
            computable.name
        } else {
            null
        }
    }.toMutableSet()

    private fun findNewName(used: Set<String>, name: String): String {
        var counter = 0
        var current = name
        while(used.contains(current)) {
            current = name + counter.toString()
            counter++
        }
        return current
    }
}