package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.analysis.VoidType
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeVoid

object FunctionPrintln : InbuiltFunction("println", listOf("value" to DataType.ANYTHING), VoidType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.println(context.getDataOfType<Data>("value").toScriptString())
        return DataTypeVoid
    }
}