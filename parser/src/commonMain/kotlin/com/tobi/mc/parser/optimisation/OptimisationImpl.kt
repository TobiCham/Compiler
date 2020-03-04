package com.tobi.mc.parser.optimisation

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.SyntaxOptimiser
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.updateComponentAtIndex
import com.tobi.util.DescriptionMeta
import com.tobi.util.TypeNameConverter

internal class OptimisationImpl(val optimisations: Array<Optimisation<*>>) : SyntaxOptimiser {

    override val description: DescriptionMeta = SimpleDescription(
        "Optimisation",
        "Applies optimisations to the syntax tree to improve execution speed and remove redundant operations"
    )

    override val optimisationDescriptions: List<DescriptionMeta> = optimisations.map(Optimisation<*>::description)

    override fun processProgram(program: Program): Program {
        return program.map {
            optimise(it) as FunctionDeclaration
        }
    }

    override fun optimise(computable: Computable): Computable {
        var modified: Boolean
        var currentValue = computable
        do {
            val result = currentValue.optimiseTree()
            if(result.modified && result.newValue != null) {
                currentValue = result.newValue
            }
            modified = result.modified
        } while(modified)

        return currentValue
    }

    private fun Computable.optimiseTree(): Result {
        var modified = false

        for((i, component) in this.getComponents().withIndex()) {
            val result = component.optimiseTree()
            if(!result.modified) continue
            if(result.newValue != null) {
                this.updateComponentAtIndex(i, result.newValue)
                return Result(true, null)
            }
            modified = true
        }
        for(optimisation in optimisations) {
            val result = optimisation.runOptimisation(this)
            if(result.newValue != null) {
                return result
            }
            if(result.modified) {
                modified = true
            }
        }
        return Result(modified, null)
    }

    private fun Optimisation<*>.runOptimisation(computable: Computable): Result {
        if(!this.accepts(computable)) {
            return Result(false, null)
        }
        var result: Result? = null
        val replace: (Computable) -> Boolean = {
            if(result != null) {
                throw IllegalStateException("Cannot call 'replace' twice")
            }
            result = Result(true, it)
            true
        }

        @Suppress("UNCHECKED_CAST")
        this as Optimisation<Computable>
        computable.run {
            val modified = this.optimise(replace)
            if(!modified && result != null) {
                throw IllegalStateException("Expected to return modified as true, didn't")
            }
            if(result == null) {
                result = Result(modified, null)
            }
        }
        if(result!!.modified) {
            println("Optimise ${TypeNameConverter.getTypeName(computable)} (${this.description.name})")
//            println(ProgramToString.toString(result!!.newValue ?: computable))
        }
        return result!!
    }

    private companion object {
        data class Result(val modified: Boolean, val newValue: Computable?)
    }
}