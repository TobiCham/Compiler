package com.tobi.mc.computable

import com.tobi.mc.SourceObject
import com.tobi.mc.computable.data.Data

interface Computable : SourceObject {

    val description: String

    fun getNodes(): Iterable<Computable>

    suspend fun compute(context: Context, environment: ExecutionEnvironment): Data
}