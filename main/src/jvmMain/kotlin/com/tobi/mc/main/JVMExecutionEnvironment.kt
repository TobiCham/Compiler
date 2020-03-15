package com.tobi.mc.main

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.ExecutionEnvironment

object JVMExecutionEnvironment : ExecutionEnvironment {

    override fun print(message: String) = kotlin.io.print(message)
    override fun println(message: String) = kotlin.io.println(message)

    override suspend fun readLine(): String {
        return kotlin.io.readLine() ?: throw ScriptException("End of input stream")
    }
}