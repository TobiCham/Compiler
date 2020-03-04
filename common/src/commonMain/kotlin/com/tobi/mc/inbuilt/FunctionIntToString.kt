package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.Data
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.IntType
import com.tobi.mc.type.StringType

object FunctionIntToString : InbuiltFunction("intToString", listOf("value" to IntType), StringType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val value = context.getDataOfType<Data>("value")
        return DataTypeString(value.toScriptString())
    }
}