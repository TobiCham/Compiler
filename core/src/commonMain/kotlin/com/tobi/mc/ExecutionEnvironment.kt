package com.tobi.mc

interface ExecutionEnvironment {

    fun print(message: String)

    fun println(message: String)

    fun readLine(): String
}