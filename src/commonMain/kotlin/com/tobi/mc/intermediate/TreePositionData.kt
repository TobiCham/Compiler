package com.tobi.mc.intermediate

import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.intermediate.code.EnvironmentVariable
import com.tobi.mc.intermediate.code.StackVariable
import com.tobi.mc.intermediate.code.TacMutableVariable

data class TreePositionData(
    val contextDepth: Int,
    val functionCount: Int,
    val currentBlock: ExpressionSequence,
    val environmentVariables: MutableMap<Pair<Int, String>, Int>,
    val stackVariables: MutableSet<String>,
    val variableNameMapping: MutableMap<Pair<Int, String>, String>
) {

    fun enterFunction(function: FunctionDeclaration): TreePositionData = TreePositionData(
        contextDepth + 1,
        functionCount + 1,
        function.body,
        HashMap(environmentVariables),
        LinkedHashSet(), LinkedHashMap()
    )

    fun enterSequence(sequence: ExpressionSequence): TreePositionData = this.copy(
        contextDepth = contextDepth + 1,
        currentBlock = sequence
    )

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
        val mappedName = variableNameMapping[rootDepth to name]
            ?: throw IllegalStateException("Variable $name not found")
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