package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context

class DefaultContext : Context(null) {

    init {
        defineFunctions(
            FunctionPrintInt, FunctionPrintString,
            FunctionReadInt, FunctionReadString,
            FunctionIntToString,
            FunctionConcat,
            FunctionUnixTime,
            FunctionSleep,
            FunctionExit
        )
    }

    private fun defineFunctions(vararg functions: InbuiltFunction) {
        functions.forEach(this::defineFunction)
    }

    private fun defineFunction(function: InbuiltFunction) {
        defineVariable(function.functionDescription.name, function)
    }
}