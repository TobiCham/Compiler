package com.tobi.mc.parser

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack
import com.tobi.mc.util.Stack

object ContextIndexResolver {

    /**
     * Calculates and sets which context each variable in the program is referring to
     */
    fun calculateVariableContexts(program: Program) {
        val contexts = ArrayListStack<MutableSet<String>>()
        contexts.push(program.context.getVariables().keys.toHashSet())
        program.calculate(contexts)
    }

    fun Computable.calculate(contexts: MutableStack<MutableSet<String>>) {
        if(this is ExpressionSequence) {
            contexts.push(HashSet())
        } else if(this is FunctionPrototype) {
            addVariable(this, contexts)
        }
        if(this is FunctionDeclaration) {
            contexts.push(this.parameters.map { it.name }.toHashSet())
        }
        for(node in this.getNodes()) {
            node.calculate(contexts)
        }
        if(this is VariableDeclaration) {
            addVariable(this, contexts)
        }

        if(this is ExpressionSequence || this is FunctionDeclaration) {
            contexts.pop()
        }
        if(this is VariableContext) {
            this.contextIndex = findVariable(this, contexts)
        }
    }

    private fun addVariable(variable: VariableReference, contexts: MutableStack<MutableSet<String>>) {
        val context = contexts.peek()
        if(!context.add(variable.name)) {
            throw ParseException("Variable already declared", variable)
        }
    }

    private fun findVariable(variable: VariableContext, contexts: Stack<MutableSet<String>>): Int {
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