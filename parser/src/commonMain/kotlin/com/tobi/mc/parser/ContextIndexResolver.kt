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
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack
import com.tobi.mc.util.Stack

object ContextIndexResolver {

    /**
     * Calculates and sets which context each variable in the program is refering to
     */
    fun calculateVariableContexts(program: Program) {
        val contexts = ArrayListStack<MutableSet<String>>()
        contexts.push(program.context.getVariables().keys.toHashSet())
        program.calculate(contexts)
    }

    private fun Computable.calculate(contexts: MutableStack<MutableSet<String>>) {
        if(this is ExpressionSequence) {
            contexts.push(HashSet())
        } else if(this is DefineVariable || this is FunctionDeclaration) {
            contexts.peek().add((this as VariableReference).name)
        }
        if(this is FunctionDeclaration) {
            contexts.push(this.parameters.map { it.name }.toHashSet())
        }
        for(component in this.getComponents()) {
            component.calculate(contexts)
        }
        if(this is ExpressionSequence || this is FunctionDeclaration) {
            contexts.pop()
        }
        if(this is GetVariable) {
            this.contextIndex = findVariable(this, contexts)
        } else if(this is SetVariable) {
            this.contextIndex = findVariable(this, contexts)
        }
    }

    private fun findVariable(variable: VariableReference, contexts: Stack<MutableSet<String>>): Int {
        val name = variable.name
        var i = contexts.size - 1
        while(i >= 0) {
            val context = contexts[i]
            if(context.contains(name)) {
                return contexts.size - i - 1
            }
            i--
        }
        throw ParseException("Unknown variable $name", variable)
    }
}