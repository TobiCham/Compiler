package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType
import com.tobi.mc.util.TimeUtils

object FunctionUnixTime : InbuiltFunction(emptyList(), IntType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        return DataTypeInt(TimeUtils.unixTimeMillis())
    }
}