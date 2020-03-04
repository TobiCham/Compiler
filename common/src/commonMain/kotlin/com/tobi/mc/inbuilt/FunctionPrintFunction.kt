package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeClosure
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.FunctionType
import com.tobi.mc.type.UnknownParameters
import com.tobi.mc.type.UnknownType
import com.tobi.mc.type.VoidType

object FunctionPrintFunction : InbuiltFunction("printFunction", listOf("value" to FunctionType(UnknownType, UnknownParameters)), VoidType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeVoid {
        environment.print(context.getDataOfType<DataTypeClosure>("value").toScriptString())
        return DataTypeVoid
    }
}