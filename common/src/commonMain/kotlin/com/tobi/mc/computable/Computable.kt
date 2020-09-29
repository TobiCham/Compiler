package com.tobi.mc.computable

import com.tobi.mc.computable.data.Data

interface Computable {

    val description: String

    suspend fun compute(context: Context, environment: ExecutionEnvironment): Data
}