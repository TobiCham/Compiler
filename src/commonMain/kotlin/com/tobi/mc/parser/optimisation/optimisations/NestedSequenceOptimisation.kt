package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object NestedSequenceOptimisation : ASTInstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Nested sequence", """
        It can occur that expression sequences are nested, e.g. when the body of an if will always succeed.
        This will split those sequences out
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): OptimisationResult<ExpressionSequence> {
        val newOps = ArrayList<Computable>(this.expressions.size)
        var changed = false

        for(expression in this.expressions) {
            if(expression is ExpressionSequence) {
                val usedVariables = this.findUnavailableVariables()
                expression.renameVariables(usedVariables)
                expression.decrementVariableContexts()
                newOps.addAll(expression.expressions)
                changed = true
            } else {
                newOps.add(expression)
            }
        }
        if(changed) {
            return newValue(ExpressionSequence(newOps))
        }
        return noOptimisation()
    }

    private fun ExpressionSequence.renameVariables(usedVariables: MutableSet<String>) {
        for(expression in this.expressions) {
            if(expression is FunctionDeclaration || expression is VariableDeclaration) {
                if(usedVariables.contains((expression as VariableReference).name)) {
                    val newName = findNewName(usedVariables, expression.name)
                    this.renameVariable(expression.name, newName)
                    expression.name = newName
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
        for((node, depth) in this.traverseWithDepth()) {
            if(node is VariableContext && node.contextIndex >= depth) {
                node.contextIndex--
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