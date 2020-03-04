package com.tobi.mc.parser.types

import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.type.ExpandedType
import com.tobi.mc.type.FunctionType
import com.tobi.mc.type.KnownParameters
import com.tobi.mc.type.UnknownType

internal class VariableTypeState(private val parent: VariableTypeState?) {

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

    /**
     * Initialises a new function
     * @return the new state
     */
    fun initialiseFunction(function: FunctionDeclaration, mapping: (DataType) -> ExpandedType): Pair<VariableTypeState, FunctionType> {
        val params = function.parameters.map { (_, type) -> mapping(type) }

        val returnType = if(function.returnType != null) mapping(function.returnType!!) else UnknownType
        val functionType = FunctionType(
            returnType,
            KnownParameters(params)
        )
        define(function.name, functionType)

        val newState = VariableTypeState(this)
        for ((paramName, paramType) in function.parameters) {
            newState.define(paramName, mapping(paramType))
        }
        return Pair(newState, functionType)
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