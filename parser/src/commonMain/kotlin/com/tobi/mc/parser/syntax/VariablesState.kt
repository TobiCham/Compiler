package com.tobi.mc.parser.syntax

import com.tobi.mc.ParseException
import com.tobi.mc.computable.variable.VariableReference

internal data class VariablesState(val parent: VariablesState?) {

    private val variables: MutableSet<String> = HashSet()

    fun exists(name: String) = find(name, this)

    fun ensureExists(variable: VariableReference) {
        if(!exists(variable.name)) {
            throw ParseException("Unknown variable", variable)
        }
    }

    fun canBeDefined(name: String) = !variables.contains(name)

    fun ensureCanBeDefined(variable: VariableReference) {
        if(!canBeDefined(variable.name)) {
            throw ParseException("Variable already defined", variable)
        }
    }

    fun define(vararg names: String): VariablesState = apply {
        variables.addAll(names)
    }

    override fun toString(): String = buildString {
        append('[')
        if(parent != null) {
            append(parent.toString())

            if(variables.isNotEmpty()) append(", ")
        }
        for((i, variable) in variables.withIndex()) {
            if(i != 0) append(", ")
            append(variable)
        }
        append(']')
    }

    private companion object {

        tailrec fun find(name: String, scope: VariablesState?): Boolean {
            if(scope == null) return false
            if(scope.variables.contains(name)) return true
            return find(
                name,
                scope.parent
            )
        }
    }
}