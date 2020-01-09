package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.ExecutionEnvironment
import com.tobi.mc.analysis.StringType
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeString

object FunctionConcat : InbuiltFunction("concat", listOf("string1" to DataType.STRING, "string2" to DataType.STRING), StringType) {

    override fun compute(context: Context, environment: ExecutionEnvironment): DataTypeString {
        val string1 = context.getDataOfType<DataTypeString>("string1").value
        val string2 = context.getDataOfType<DataTypeString>("string2").value

        return DataTypeString(string1 + string2)
    }
}