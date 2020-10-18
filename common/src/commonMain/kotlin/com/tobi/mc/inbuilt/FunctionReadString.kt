package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.type.StringType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object FunctionReadString : InbuiltFunction(emptyList(), StringType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("readString", "Reads in a string from the console")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        return DataTypeString(environment.readLine())
    }
}