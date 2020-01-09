package com.tobi.mc

import com.tobi.mc.inbuilt.*

class DefaultContext : Context(null) {

    val defaultVariables: Set<InbuiltVariable> = HashSet()

    init {
        addVariables(
            FunctionPrint,
            FunctionPrintln,
            FunctionReadInt,
            FunctionReadString,
            FunctionToString,
            FunctionConcat
        )
    }

    private fun addVariables(vararg functions: InbuiltFunction) {
        functions.forEach(this::addVariable)
    }

    private fun addVariable(variable: InbuiltVariable) {
        defineVariable(variable.name, variable.computeData(this))
        (this.defaultVariables as MutableSet).add(variable)
    }
}