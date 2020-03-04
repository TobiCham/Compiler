package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType

object FunctionReadInt : InbuiltFunction("readInt", emptyList(), IntType) {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): DataTypeInt {
        var result: Int? = null
        while(result == null) {
            result = environment.readLine().toIntOrNull()
            if(result == null) environment.println("Invalid integer. Enter again:")
        }
        return DataTypeInt(result)
    }
}