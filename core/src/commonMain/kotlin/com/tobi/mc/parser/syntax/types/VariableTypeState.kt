package com.tobi.mc.parser.syntax.types

import com.tobi.mc.analysis.ExpandedType
import com.tobi.mc.analysis.FunctionType
import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.computable.ParameterList
import com.tobi.mc.computable.data.DataType

class VariableTypeState(private val parent: VariableTypeState?) {

    private val variables: MutableMap<String, ExpandedType> = HashMap()
    private val functionParams: MutableMap<String, ParameterList> = HashMap()

    fun getType(name: String) = find(name, VariableTypeState::variables)
    fun getFunctionParams(name: String): ParameterList? = find(name, VariableTypeState::functionParams)

    fun define(name: String, type: ExpandedType) = apply {
        variables[name] = type
    }

    fun defineFunctionParams(name: String, params: ParameterList) = apply {
        functionParams[name] = params
    }

    /**
     * Initialises a new function
     * @return the new state
     */
    fun initialiseFunction(function: FunctionDeclaration, mapping: (DataType) -> ExpandedType): VariableTypeState {
        define(function.name, FunctionType(mapping(function.returnType)))
        defineFunctionParams(function.name, function.parameters)

        val newState = VariableTypeState(this)
        for ((paramName, paramType) in function.parameters) {
            newState.define(paramName, mapping(paramType))
        }
        return newState
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