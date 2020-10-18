package com.tobi.mc.inbuilt

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.control.FlowInterrupt
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.type.IntType
import com.tobi.mc.type.VoidType
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object FunctionExit : InbuiltFunction(listOf("exitCode" to IntType), VoidType) {

    override val functionDescription: DescriptionMeta = SimpleDescription("exit", "Exits the program with an exit code")

    override suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        val code = arguments[0] as DataTypeInt
        throw FlowInterrupt.Exit(code)
    }
}