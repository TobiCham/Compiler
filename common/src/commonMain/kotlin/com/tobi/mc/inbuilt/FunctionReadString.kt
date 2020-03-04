package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.StringType

object FunctionReadString : InbuiltFunction("readString", emptyList(), StringType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        return DataTypeString(environment.readLine())
    }
}