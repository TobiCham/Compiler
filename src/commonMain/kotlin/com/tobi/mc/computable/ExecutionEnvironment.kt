package com.tobi.mc.computable

interface ExecutionEnvironment {

    fun print(message: String)

    fun println(message: String)

    suspend fun readLine(): String
}