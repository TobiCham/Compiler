package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.*
import com.tobi.mc.parser.experimental.VariableRenamer
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.copyAndReplaceIndex
import com.tobi.mc.util.copyExceptIndex
import com.tobi.mc.util.getAfterIndex

internal object NestedSequenceOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    override val description: DescriptionMeta = SimpleDescription("Nested sequence", """
        It can occur that expression sequences are nested, e.g. when the body of an if will always succeed.
        This will split those sequences out
    """.trimIndent())

    override fun ExpressionSequence.optimise(replace: (Computable) -> Boolean): Boolean {
        for((i, operation) in this.operations.withIndex()) {
            if(operation is ExpressionSequence && operation.operations.isNotEmpty()) {
                val otherOps = this.operations.copyExceptIndex(i)
                operation.renameVariables(otherOps)
                val newOps = this.operations.copyAndReplaceIndex(i, operation.operations)

                return replace(ExpressionSequence(newOps))
            }
        }
        return false
    }

    private fun ExpressionSequence.renameVariables(otherOps: List<Computable>) {
        val referencedVariables = ExpressionSequence(otherOps).findUsedVariables()
        val selfReferenced = this.findUsedVariables()

        for((i, operation) in this.operations.withIndex()) {
            if(operation is DefineVariable || operation is FunctionDeclaration) {
                val name = (operation as VariableReference).name
                if(referencedVariables.contains(name)) {
                    val newName = findNewName(referencedVariables, selfReferenced, name)
                    operation.name = newName

                    VariableRenamer.renameVariable(ExpressionSequence(this.operations.getAfterIndex(i)), name, newName)
                    referencedVariables.add(newName)
                } else {
                    referencedVariables.add(name)
                }
            }
        }
    }

    private fun findNewName(used: Set<String>, usedInBlock: Set<String>,original: String): String {
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
        if(this is DefineVariable || this is FunctionDeclaration) {
            val name = (this as VariableReference).name
            if(depth > 0) definedVariables.add(name)
            else names.add(name)
        }
        if(this is FunctionDeclaration) {
            definedVariables.addAll(this.parameters.map { it.first })
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