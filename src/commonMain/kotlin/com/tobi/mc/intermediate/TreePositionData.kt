package com.tobi.mc.intermediate

import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.intermediate.construct.code.EnvironmentVariable
import com.tobi.mc.intermediate.construct.code.StackVariable
import com.tobi.mc.intermediate.construct.code.TacMutableVariable

data class TreePositionData(
    val contextDepth: Int, //Context depth
    val functionCount: Int,
    val currentBlock: ExpressionSequence,
    val blockLine: Int,
    val environmentVariables: MutableMap<Pair<Int, String>, Int>,
    val stackVariables: MutableSet<String>,
    val variableNameMapping: MutableMap<Pair<Int, String>, String>
) {

    fun getVariable(index: Int, name: String): TacMutableVariable {
        val rootDepth = contextDepth - index
        val environmentVariableIndex = environmentVariables[rootDepth to name]
        if(environmentVariableIndex != null) {
            return EnvironmentVariable(name, functionCount - environmentVariableIndex)
        }
        return getStackVariable(index, name)
    }

    fun createEnvironmentVariable(name: String): EnvironmentVariable {
        environmentVariables[contextDepth to name] = functionCount
        return EnvironmentVariable(name, 0)
    }

    fun createStackVariable(name: String): StackVariable {
        val uniqueName = findUniqueName(name)
        variableNameMapping[contextDepth to name] = uniqueName
        stackVariables.add(uniqueName)
        return StackVariable(uniqueName)
    }

    private fun getStackVariable(index: Int, name: String): StackVariable {
        val rootDepth = contextDepth - index
        val mappedName = variableNameMapping[rootDepth to name] ?: throw IllegalStateException("Variable $name not found")
        return StackVariable(mappedName)
    }

    private fun findUniqueName(name: String): String {
        if(!stackVariables.contains(name)) {
            return name
        }

        var check = name
        var index = 0

        while(stackVariables.contains(check)) {
            check = "$name\$$index"
            index++
        }
        return check
    }
}