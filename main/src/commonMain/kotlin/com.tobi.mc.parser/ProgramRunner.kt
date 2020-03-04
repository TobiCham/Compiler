package com.tobi.mc.parser

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt

class ProgramRunner {

    suspend fun run(program: Program, environment: ExecutionEnvironment, context: Context = DefaultContext()): Int? {
        val newContext = Context(context)
        for (function in program) {
            val closure = function.compute(newContext, environment)

            if(isMainFunction(function)) {
                val result = FunctionCall(closure, emptyList()).compute(newContext, environment)
                return if(result is DataTypeInt) result.value else null
            }
        }
        throw ScriptException("No main function found to run")
    }

    private fun isMainFunction(function: FunctionDeclaration): Boolean =
        function.name == "main" && (function.returnType == DataType.VOID  || function.returnType == DataType.INT) && function.parameters.isEmpty()

}