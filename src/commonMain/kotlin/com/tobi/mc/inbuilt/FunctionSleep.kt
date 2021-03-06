package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.type.IntType
import com.tobi.mc.type.VoidType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription
import kotlinx.coroutines.delay

object FunctionSleep : InbuiltFunction(listOf("millis" to IntType), VoidType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("sleep", "Sleeps a specified number of milliseconds")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeVoid {
        val millis = (arguments[0] as DataTypeInt).value
        if(millis > 0) {
            delay(millis)
        }
        return DataTypeVoid()
    }
}