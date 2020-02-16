package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.syntax.types.StringType

object FunctionReadString : InbuiltFunction("readString", emptyList(), StringType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        return DataTypeString(environment.readLine())
    }
}