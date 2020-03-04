package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.StringType
import com.tobi.mc.type.VoidType

object FunctionPrintString : InbuiltFunction("printString", listOf("value" to StringType), VoidType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.print(context.getDataOfType<DataTypeString>("value").toScriptString())
        return DataTypeVoid
    }
}