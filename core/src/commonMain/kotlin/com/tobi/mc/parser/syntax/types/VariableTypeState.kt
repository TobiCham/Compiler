package com.tobi.mc.parser.syntax.types

import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.computable.data.DataType

class VariableTypeState(private val parent: VariableTypeState?) {

    private val variables: MutableMap<String, AnalysisType> = HashMap()

    fun getType(name: String) = find(name, VariableTypeState::variables)

    fun define(name: String, type: AnalysisType) = apply {
        variables[name] = type
    }

    /**
     * Initialises a new function
     * @return the new state
     */
    fun initialiseFunction(function: FunctionDeclaration, mapping: (DataType) -> AnalysisType): Pair<VariableTypeState, AnalysisFunctionType> {
        val params = function.parameters.map { (_, type) -> mapping(type) }
        val functionType = AnalysisFunctionType(mapping(function.returnType), AnalysisKnownParams(params))
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