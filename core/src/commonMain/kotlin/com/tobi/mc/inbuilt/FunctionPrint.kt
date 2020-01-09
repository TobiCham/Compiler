package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.analysis.VoidType
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeVoid

object FunctionPrint : InbuiltFunction("print", listOf("value" to DataType.ANYTHING), VoidType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.print(context.getDataOfType<Data>("value").toScriptString())
        return DataTypeVoid
    }
}