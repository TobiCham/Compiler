package com.tobi.mc.computable.function

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.variable.VariableReference

data class FunctionPrototype(val function: FunctionDeclaration) : Computable, VariableReference {

    override val description: String = "function prototype"

    override var name: String = function.name

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        throw ScriptException("Function prototypes are for internal use only", null)
    }

    override var sourceRange = function.sourceRange
}