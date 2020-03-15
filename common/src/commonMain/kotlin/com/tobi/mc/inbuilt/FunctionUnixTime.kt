package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.Data
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType
import com.tobi.mc.util.TimeUtils

object FunctionUnixTime : InbuiltFunction("unixTime", emptyList(), IntType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        return DataTypeInt(TimeUtils.unixTimeMillis())
    }
}