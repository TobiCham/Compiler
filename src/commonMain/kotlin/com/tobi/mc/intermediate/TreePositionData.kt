package com.tobi.mc.intermediate

import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.intermediate.construct.code.EnvironmentVariable
import com.tobi.mc.intermediate.construct.code.StackVariable
import com.tobi.mc.intermediate.construct.code.TacMutableVariable

data class TreePositionData(
    val totalDepth: Int,
    val functionCount: Int,
    val currentBlock: ExpressionSequence,
    val blockLine: Int,
    val environmentVariables: MutableMap<Pair<Int, String>, Int>,
    val stackVariables: MutableMap<String, DataType>,
    val variableNameMapping: MutableMap<Pair<Int, String>, String>
) {

    fun getVariable(index: Int, name: String): TacMutableVariable {
        val rootDepth = totalDepth - index
        val environmentVariableIndex = environmentVariables[rootDepth to name]
        if(environmentVariableIndex != null) {
            return EnvironmentVariable(name, environmentVariableIndex)
        }
        return getStackVariable(index, name)
    }

    fun createEnvironmentVariable(name: String): EnvironmentVariable {
        environmentVariables[totalDepth to name] = functionCount
        return EnvironmentVariable(name, functionCount)
    }

    fun createStackVariable(name: String, type: DataType): StackVariable {
        val uniqueName = findUniqueName(name)
        variableNameMapping[totalDepth to name] = uniqueName
        stackVariables[uniqueName] = type
        return StackVariable(uniqueName)
    }

    private fun getStackVariable(index: Int, name: String): StackVariable {
        val rootDepth = totalDepth - index
        val mappedName = variableNameMapping[rootDepth to name] ?: throw IllegalStateException("Variable $name not found")
        return StackVariable(mappedName)
    }

    private fun findUniqueName(name: String): String {
        if(!stackVariables.containsKey(name)) {
            return name
        }

        var check = name
        var index = 0

        while(stackVariables.containsKey(check)) {
            check = "$name\$$index"
            index++
        }
        return check
    }
}