package com.tobi.mc.computable

import com.tobi.mc.ScriptException

open class Context(parent: Context?) {

    private val variables: List<MutableMap<String, Data>>

    init {
        variables = ArrayList<MutableMap<String, Data>>().apply {
            add(LinkedHashMap())
            if(parent != null) {
                addAll(parent.variables)
            }
        }
    }

    fun getDataType(name: String, contextIndex: Int): Data? {
        checkIndex(contextIndex)
        return variables[contextIndex][name]
    }

    inline fun <reified T : Data> getDataOfType(name: String, contextIndex: Int): T {
        return getDataType(name, contextIndex) as T
    }

    fun defineVariable(name: String, data: Data) {
        val vars = variables[0]
        if(vars.containsKey(name)) {
            throw ScriptException("Variable '$name' already exists, cannot redefine")
        }
        vars[name] = data
    }

    fun setVariable(name: String, contextIndex: Int, data: Data) {
        checkIndex(contextIndex)
        val vars = variables[contextIndex]
        val existing = vars[name] ?: throw ScriptException("Unknown variable '$name'")
        if(existing.type != data.type) {
            throw ScriptException("Cannot reassign variable $name:${existing.type} to ${data.type}")
        }
        vars[name] = data
    }

    fun getVariables() = variables[0].toMap()

    private fun checkIndex(index: Int) {
        if(index < 0 || index >= variables.size) {
            throw IllegalArgumentException("Invalid index $index")
        }
    }
}