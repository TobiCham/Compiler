package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.parser.syntax.types.IntType
import com.tobi.mc.parser.syntax.types.VoidType

object FunctionPrintInt : InbuiltFunction("printInt", listOf("value" to IntType), VoidType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.println(context.getDataOfType<DataTypeInt>("value").toScriptString())
        return DataTypeVoid
    }
}