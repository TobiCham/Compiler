package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.StringType

object FunctionReadString : InbuiltFunction(emptyList(), StringType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        return DataTypeString(environment.readLine())
    }
}