package com.tobi.mc.computable

import com.tobi.mc.ComputableResult
import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment

interface Computable {

    val components: Array<Computable>

    fun compute(context: Context, environment: ExecutionEnvironment): ComputableResult

    fun optimise(): Computable
}