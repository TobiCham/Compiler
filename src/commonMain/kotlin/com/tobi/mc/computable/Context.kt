package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceObject
import com.tobi.mc.computable.data.Data

open class Context private constructor(private val variables: List<MutableMap<String, Data>>) {

    constructor(parent: Context?): this(ArrayList<MutableMap<String, Data>>().apply {
        add(LinkedHashMap())
        if(parent != null) {
            addAll(parent.variables)
        }
    })

    fun getDataType(name: String, contextIndex: Int): Data? {
        checkIndex(contextIndex)
        return variables[contextIndex][name]
    }

    fun defineVariable(name: String, data: Data, obj: SourceObject? = null) {
        val vars = variables[0]
        if(vars.containsKey(name)) {
            throw ScriptException("Variable '$name' already exists, cannot redefine", obj)
        }
        vars[name] = data
    }

    fun setVariable(name: String, contextIndex: Int, data: Data, obj: SourceObject) {
        checkIndex(contextIndex)
        val vars = variables[contextIndex]
        val existing = vars[name] ?: throw ScriptException("Unknown variable '$name'", obj)
        if(existing.type != data.type) {
            throw ScriptException("Cannot reassign variable $name:${existing.type} to ${data.type}", obj)
        }
        vars[name] = data
    }

    fun getVariables() = variables[0].toMap()

    fun copy() = Context(this.variables.map { LinkedHashMap(it) })

    private fun checkIndex(index: Int) {
        if(index < 0 || index >= variables.size) {
            throw IllegalArgumentException("Invalid index $index")
        }
    }
}