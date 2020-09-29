package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.StringType
import com.tobi.mc.type.VoidType

object FunctionPrintString : InbuiltFunction(listOf("value" to StringType), VoidType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeVoid {
        val value = arguments[0] as DataTypeString
        environment.print(value.value)

        return DataTypeVoid
    }
}