package com.tobi.mc.main

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.ExecutionEnvironment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object JVMExecutionEnvironment : ExecutionEnvironment {

    override fun print(message: String) = kotlin.io.print(message)
    override fun println(message: String) = kotlin.io.println(message)

    override suspend fun readLine(): String = withContext(Dispatchers.IO) {
        return@withContext kotlin.io.readLine() ?: throw ScriptException("End of input stream", null)
    }
}