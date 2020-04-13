package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.FunctionType
import com.tobi.mc.type.StringType
import com.tobi.mc.type.UnknownParameters
import com.tobi.mc.type.UnknownType

object FunctionFunctionToString : InbuiltFunction("functionToString", listOf("value" to FunctionType(UnknownType, UnknownParameters)), StringType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val value = context.getDataOfType<DataTypeClosure>("value", 1)
        return DataTypeString(value.toScriptString())
    }
}