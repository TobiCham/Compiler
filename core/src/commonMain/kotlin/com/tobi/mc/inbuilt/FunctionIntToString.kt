package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.syntax.types.IntType
import com.tobi.mc.parser.syntax.types.StringType

object FunctionIntToString : InbuiltFunction("intToString", listOf("value" to IntType), StringType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val value = context.getDataOfType<Data>("value")
        return DataTypeString(value.toScriptString())
    }
}