package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.StringType

object FunctionConcat : InbuiltFunction("concat", listOf("string1" to StringType, "string2" to StringType), StringType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val string1 = context.getDataOfType<DataTypeString>("string1", 1).value
        val string2 = context.getDataOfType<DataTypeString>("string2", 1).value

        return DataTypeString(string1 + string2)
    }
}