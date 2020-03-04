package com.tobi.mc.computable

import com.tobi.mc.ScriptException

open class Context(private val parent: Context?) {

    private val variables: MutableMap<String, Data> = LinkedHashMap()

    fun getDataType(name: String): Data? {
        traverseParents {
            val data = it.variables[name]
            if(data != null) return data
        }
        return null
    }

    inline fun <reified T : Data> getDataOfType(name: String): T {
        return getDataType(name) as T
    }

    fun defineVariable(name: String, data: Data) {
        if(variables.containsKey(name)) {
            throw ScriptException("Variable '$name' already exists, cannot redefine")
        }
        variables[name] = data
    }

    fun setVariable(name: String, data: Data) {
        traverseParents {
            val existing = it.variables[name]
            if(existing != null) {
                if(existing.type != data.type) {
                    throw ScriptException("Cannot reassign variable $name:${existing.type} to ${data.type}")
                }
                it.variables[name] = data
                return
            }
        }
        throw ScriptException("Unknown variable '$name'")
    }

    fun getVariables() = variables.toMap()

    private inline fun traverseParents(action: (Context) -> Unit) {
        var currentContext: Context? = this
        while(currentContext != null) {
            action(currentContext)
            currentContext = currentContext.parent
        }
    }
}