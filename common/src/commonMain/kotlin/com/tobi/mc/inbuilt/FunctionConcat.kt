package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.StringType

object FunctionConcat : InbuiltFunction(listOf(
    "string1" to StringType,
    "string2" to StringType
), StringType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeString {
        val string1 = arguments[0] as DataTypeString
        val string2 = arguments[1] as DataTypeString

        return DataTypeString(string1.value + string2.value)
    }
}