package com.tobi.mc.parser

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta

object ContextIndexResolver : ParserOperation {

    override val description: DescriptionMeta = SimpleDescription("Resolves context", """
        Calculates which context variables are referring to
    """.trimIndent())

    override fun processProgram(program: Program) {
        val contexts = ArrayList<MutableSet<String>>()
        contexts.add(program.context.getVariables().keys.toHashSet())
        program.calculate(contexts)
    }

    private fun Computable.calculate(contexts: ArrayList<MutableSet<String>>) {
        if(this is ExpressionSequence) {
            contexts.add(HashSet())
        } else if(this is DefineVariable || this is FunctionDeclaration) {
            contexts.last().add((this as VariableReference).name)
        }
        if(this is FunctionDeclaration) {
            contexts.add(this.parameters.map { it.name }.toHashSet())
        }
        for(component in this.getComponents()) {
            component.calculate(contexts)
        }
        if(this is ExpressionSequence || this is FunctionDeclaration) {
            contexts.removeAt(contexts.size - 1)
        }
        if(this is GetVariable) {
            this.contextIndex = findVariable(this.name, contexts)
        } else if(this is SetVariable) {
            this.contextIndex = findVariable(this.name, contexts)
        }
    }

    private fun findVariable(name: String, contexts: ArrayList<MutableSet<String>>): Int {
        var i = contexts.size - 1
        while(i >= 0) {
            val context = contexts[i]
            if(context.contains(name)) {
                return contexts.size - i - 1
            }
            i--
        }
        throw ParseException("Unknown variable $name")
    }
}