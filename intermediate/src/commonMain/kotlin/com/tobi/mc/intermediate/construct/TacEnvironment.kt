package com.tobi.mc.intermediate.construct

import com.tobi.mc.computable.data.DataType
import com.tobi.mc.intermediate.TacStructure

open class TacEnvironment(val name: String, var parent: TacEnvironment?): TacStructure {

    val variables: Map<String, DataType> = LinkedHashMap()

    /**
     * @return A new [TacEnvironment] with no parent, consisting of all the variables defined by this
     * environment and all parent environments
     */
    fun collapse(): TacEnvironment {
        val newEnv = TacEnvironment(name, null)
        
        val collapsedParent = parent?.collapse()
        if(collapsedParent != null) {
            (newEnv.variables as MutableMap).putAll(collapsedParent.variables)
        }
        (newEnv.variables as MutableMap).putAll(variables)
        return newEnv
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
        fun addOffsets(environment: TacEnvironment?) {
            if(environment == null) return
            for((name, _) in environment.variables.entries.reversed()) {
                result.add(name)
            }
            addOffsets(environment.parent)
        }
        addOffsets(this)
        return result.reversed().toTypedArray()
    }

    fun addVariable(name: String, type: DataType) {
        if(type == DataType.VOID) {
            throw IllegalArgumentException("Type cannot be void")
        }
        (variables as MutableMap)[name] = type
    }

    fun getType(name: String) = variables[name]
}