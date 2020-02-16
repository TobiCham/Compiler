package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.syntax.types.UnknownType
import com.tobi.mc.parser.syntax.types.FunctionType
import com.tobi.mc.parser.syntax.types.StringType
import com.tobi.mc.parser.syntax.types.UnknownParameters

object FunctionFunctionToString : InbuiltFunction("functionToString", listOf("value" to FunctionType(UnknownType, UnknownParameters)), StringType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val value = context.getDataOfType<DataTypeClosure>("value")
        return DataTypeString(value.toScriptString())
    }
}