package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.IntType
import com.tobi.mc.type.StringType

object FunctionIntToString : InbuiltFunction(listOf("value" to IntType), StringType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeString {
        val value = arguments[0] as DataTypeInt
        return DataTypeString(value.value.toString())
    }
}