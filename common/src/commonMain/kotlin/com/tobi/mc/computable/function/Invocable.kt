package com.tobi.mc.computable.function

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataType

interface Invocable {

    val parameters: List<Parameter>

    val returnType: DataType

    suspend operator fun invoke(arguments: Array<Data>, environment: ExecutionEnvironment): Data
}