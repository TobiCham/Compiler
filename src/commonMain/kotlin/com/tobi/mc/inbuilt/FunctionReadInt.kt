package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object FunctionReadInt : InbuiltFunction(emptyList(), IntType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("readInt", "Reads in an integer from the console")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): DataTypeInt {
        var result: Long? = null
        while(result == null) {
            result = environment.readLine().toLongOrNull()
            if(result == null) environment.println("Invalid integer. Enter again:")
        }
        return DataTypeInt(result)
    }
}