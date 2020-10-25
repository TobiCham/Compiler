package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.experimental.VariableRenamer
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.*

object NestedSequenceOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Nested sequence", """
        It can occur that expression sequences are nested, e.g. when the body of an if will always succeed.
        This will split those sequences out
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        for((i, operation) in this.operations.withIndex()) {
            if(operation is ExpressionSequence && operation.operations.isNotEmpty()) {
                val otherOps = this.operations.copyExceptIndex(i)
                operation.renameVariables(otherOps)
                val newOps = this.operations.copyAndReplaceIndex(i, operation.operations)

                return ExpressionSequence(newOps)
            }
        }
        return null
    }

    private fun ExpressionSequence.renameVariables(otherOps: List<Computable>) {
        val referencedVariables = ExpressionSequence(otherOps).findUsedVariables()
        val selfReferenced = this.findUsedVariables()
        val functionRenames = HashMap<String, String>()

        for((i, operation) in this.operations.withIndex()) {
            if(operation is DefineVariable || operation is FunctionDeclaration || operation is FunctionPrototype) {
                val name = (operation as VariableReference).name
                if(operation is FunctionDeclaration && functionRenames.containsKey(operation.name)) {
                    operation.name = functionRenames[operation.name]!!
                } else if(referencedVariables.contains(name)) {
                    val newName = findNewName(referencedVariables, selfReferenced, name)
                    operation.name = newName
                    if(operation is FunctionPrototype) {
                        functionRenames[name] = newName
                    }

                    VariableRenamer.renameVariable(ExpressionSequence(this.operations.getAfterIndex(i).filter {
                        !(it is FunctionDeclaration && it.name == name)
                    }), name, newName)
                    referencedVariables.add(newName)
                } else {
                    referencedVariables.add(name)
                }
            }
        }
    }

    private fun findNewName(used: Set<String>, usedInBlock: Set<String>, original: String): String {
        var counter = 0
        var current = original
        while(used.contains(current) || usedInBlock.contains(current)) {
            current = original + counter.toString()
            counter++
        }
        return current
    }

    private fun Computable.findUsedVariables(): MutableSet<String> {
        val names = HashSet<String>()
        val defined = HashSet<String>()
        this.findUsedVariables(names, defined, -1)
        return names
    }

    private fun Computable.findUsedVariables(names: MutableSet<String>, definedVariables: MutableSet<String>, depth: Int) {
        var newDepth = depth
        if(this is DefineVariable || this is FunctionDeclaration || this is FunctionPrototype) {
            val name = (this as VariableReference).name
            if(depth > 0) definedVariables.add(name)
            else names.add(name)
        }
        if(this is FunctionDeclaration) {
            definedVariables.addAll(this.parameters.map { it.name })
        }
        if(this is SetVariable || this is GetVariable) {
            if(!definedVariables.contains((this as VariableReference).name)) {
                names.add(this.name)
            }
        }
        if(this is ExpressionSequence) {
            newDepth++
        }

        for (component in this.getComponents()) {
            component.findUsedVariables(names, definedVariables, newDepth)
        }
    }
}