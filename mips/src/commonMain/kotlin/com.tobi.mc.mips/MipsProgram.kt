package com.tobi.mc.mips

data class MipsProgram(
    val config: MipsConfiguration,
    val stringConstants: Map<String, String>,
    val functions: List<MipsFunction>
) {
    class Builder {

        private val stringConstants: MutableMap<String, String> = LinkedHashMap()
        private val functions: MutableMap<String, MipsFunction> = LinkedHashMap()

        fun addStringConstant(name: String, value: String): Builder = also {
            stringConstants[name] = value
        }

        fun addFunction(function: MipsFunction) = also {
            this.functions[function.name] = function
        }

        fun build(config: MipsConfiguration): MipsProgram = MipsProgram(config, stringConstants, functions.values.toList())
    }
}