package com.tobi.mc.parser.syntax

import com.tobi.mc.ParseException
import com.tobi.mc.computable.variable.VariableReference

data class VariablesState(val parent: VariablesState?) {

    private val variables: MutableMap<String, VariableType> = HashMap()
    private val definitionsProvided = HashSet<String>()

    fun exists(name: String) = find(name, this)

    fun ensureExists(variable: VariableReference) {
        if(!exists(variable.name)) {
            throw ParseException("Unknown variable", variable)
        }
    }

    fun ensureCanBeDefined(variable: VariableReference, type: VariableType) {
        val existingType = variables[variable.name] ?: return
        if(existingType == VariableType.FUNCTION_PROTOTYPE && type == VariableType.FUNCTION_DEFINITION) {
            if(!definitionsProvided.add(variable.name)) {
                throw ParseException("Function definition already provided", variable)
            }
        } else {
            throw ParseException("${existingType.description} already defined", variable)
        }
    }

    fun define(name: String, type: VariableType): VariablesState = apply {
        this.variables[name] = type
    }

    enum class VariableType(val description: String) {
        FUNCTION_PROTOTYPE("Function prototype"),
        FUNCTION_DEFINITION("Function definition"),
        VARIABLE("Variable")
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