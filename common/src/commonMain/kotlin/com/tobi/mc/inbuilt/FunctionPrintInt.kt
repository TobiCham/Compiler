package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.IntType
import com.tobi.mc.type.VoidType

object FunctionPrintInt : InbuiltFunction(listOf("value" to IntType), VoidType) {

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeVoid {
        val value = arguments[0] as DataTypeInt
        environment.print(value.value.toString())

        return DataTypeVoid
    }
}