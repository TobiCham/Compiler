package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.parser.syntax.types.UnknownType
import com.tobi.mc.parser.syntax.types.FunctionType
import com.tobi.mc.parser.syntax.types.UnknownParameters
import com.tobi.mc.parser.syntax.types.VoidType

object FunctionPrintFunction : InbuiltFunction("printFunction", listOf("value" to FunctionType(UnknownType, UnknownParameters)), VoidType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.println(context.getDataOfType<DataTypeInt>("value").toScriptString())
        return DataTypeVoid
    }
}