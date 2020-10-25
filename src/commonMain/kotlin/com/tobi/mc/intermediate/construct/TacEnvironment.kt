package com.tobi.mc.intermediate.construct

import com.tobi.mc.computable.data.DataType
import com.tobi.mc.intermediate.construct.code.EnvironmentVariable

open class TacEnvironment(parent: TacEnvironment?) {

    private val allVariables: List<Map<String, DataType>>

    val newVariables: Map<String, DataType>
        get() = LinkedHashMap(allVariables.first())

    init {
        allVariables = ArrayList<Map<String, DataType>>().apply {
            add(LinkedHashMap())
            if(parent != null) {
                addAll(parent.allVariables)
            }
        }
    }

    fun getVariableOffsetsAsMap(): Map<String, Int> {
        val result = LinkedHashMap<String, Int>()
        for((i, name) in getVariableOffsets().withIndex()) {
            result[name] = i
        }
        return result
    }

    fun getVariableOffsets(): Array<String> {
        val result = LinkedHashSet<String>()
        for (variable in allVariables) {
            for((name, _) in variable.entries.reversed()) {
                result.add(name)
            }
        }
        return result.reversed().toTypedArray()
    }

    fun addVariable(name: String, type: DataType) {
        if(type == DataType.VOID) {
            throw IllegalArgumentException("Type cannot be void")
        }
        (allVariables.first() as MutableMap)[name] = type
    }

    fun indexOf(variable: EnvironmentVariable): Int {
        if(variable.index < 0 || variable.index >= this.allVariables.size) {
            return -1
        }
        var index = 0
        for(i in 0 until variable.index) {
            index += allVariables[allVariables.size - i - 1].size
        }
        val map = allVariables[allVariables.size - variable.index - 1].keys.toList()
        return map.indexOf(variable.name) + index
    }
}