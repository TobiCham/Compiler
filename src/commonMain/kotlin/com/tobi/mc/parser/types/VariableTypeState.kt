package com.tobi.mc.parser.types

import com.tobi.mc.type.ExpandedType

class VariableTypeState(private val parent: VariableTypeState?) {

    private val variables: MutableMap<String, ExpandedType> = HashMap()

    fun getType(name: String) = find(name, VariableTypeState::variables)

    fun define(name: String, type: ExpandedType) = apply {
        variables[name] = type
    }

    fun set(name: String, type: ExpandedType) {
        val state = find {
            if(it.variables.containsKey(name)) it else null
        } ?: throw IllegalStateException()
        state.variables[name] = type
    }

    private inline fun <T> find(name: String, getMap: (VariableTypeState) -> Map<String, T>): T? {
        return find { getMap(it)[name] }
    }

    private inline fun <T> find(getMapping: (VariableTypeState) -> T?): T? {
        var state: VariableTypeState? = this
        while(state != null) {
            val result = getMapping(state)
            if(result != null) return result
            state = state.parent
        }
        return null
    }
}