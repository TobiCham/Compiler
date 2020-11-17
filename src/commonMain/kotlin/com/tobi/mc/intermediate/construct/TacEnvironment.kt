package com.tobi.mc.intermediate.construct

import com.tobi.mc.intermediate.construct.code.EnvironmentVariable

open class TacEnvironment(parent: TacEnvironment?) {

    val variables: List<Set<String>>

    val newVariables: Set<String>
        get() = LinkedHashSet(variables.first())

    init {
        variables = ArrayList<Set<String>>().apply {
            add(LinkedHashSet())
            if(parent != null) {
                addAll(parent.variables)
            }
        }
    }

    fun addVariable(name: String) = (variables.first() as MutableSet).add(name)

    fun indexOf(variable: EnvironmentVariable): Int {
        if(variable.index < 0 || variable.index >= this.variables.size) {
            return -1
        }
        var index = 0
        for(i in 0 until variable.index) {
            index += variables[i].size
        }
        val setIndex = variables[variable.index].indexOf(variable.name)
        if(setIndex < 0) {
            return -1
        }
        return index + setIndex
    }
}