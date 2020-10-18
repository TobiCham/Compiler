package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription
import com.tobi.mc.util.TimeUtils

object FunctionUnixTime : InbuiltFunction(emptyList(), IntType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("unixTime", "Returns an integer representing the unix time in milliseconds")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        return DataTypeInt(TimeUtils.unixTimeMillis())
    }
}