package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.IntType
import com.tobi.mc.type.VoidType

object FunctionPrintInt : InbuiltFunction("printInt", listOf("value" to IntType), VoidType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.print(context.getDataOfType<DataTypeInt>("value", 1).toScriptString())
        return DataTypeVoid
    }
}