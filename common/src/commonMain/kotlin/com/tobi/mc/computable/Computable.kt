package com.tobi.mc.computable

interface Computable {

    suspend fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult
}