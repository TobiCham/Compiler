package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.analysis.IntType
import com.tobi.mc.computable.data.DataTypeInt

object FunctionReadInt : InbuiltFunction("readInt", emptyList(), IntType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeInt {
        var result: Int? = null
        while(result == null) {
            result = environment.readLine().toIntOrNull()
            if(result == null) println("Invalid integer. Enter again:")
        }
        return DataTypeInt(result)
    }
}