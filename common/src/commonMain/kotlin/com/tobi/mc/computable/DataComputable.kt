package com.tobi.mc.computable

interface DataComputable : Computable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data
}