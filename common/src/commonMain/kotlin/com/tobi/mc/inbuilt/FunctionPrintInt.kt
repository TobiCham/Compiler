package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.IntType
import com.tobi.mc.type.VoidType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object FunctionPrintInt : InbuiltFunction(listOf("value" to IntType), VoidType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("printInt", "Prints an integer to the console")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeVoid {
        val value = arguments[0] as DataTypeInt
        environment.print(value.value.toString())

        return DataTypeVoid()
    }
}