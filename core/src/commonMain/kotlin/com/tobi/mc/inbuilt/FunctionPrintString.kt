package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.parser.syntax.types.StringType
import com.tobi.mc.parser.syntax.types.VoidType

object FunctionPrintString : InbuiltFunction("printString", listOf("value" to StringType), VoidType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.println(context.getDataOfType<DataTypeString>("value").toScriptString())
        return DataTypeVoid
    }
}