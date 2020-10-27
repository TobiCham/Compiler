package com.tobi.mc.mips

data class MipsProgram(
    val config: MipsConfiguration,
    val stringConstants: Map<String, String>,
    val functions: List<MipsFunction>,
    val initialCode: List<MipsInstruction>
) {
    class Builder {

        private val stringConstants: MutableMap<String, String> = LinkedHashMap()
        private val functions: MutableMap<String, MipsFunction> = LinkedHashMap()
        var initialCode: MutableList<MipsInstruction> = ArrayList()

        fun addStringConstant(name: String, value: String): Builder = also {
            stringConstants[name] = value
        }

        fun addFunction(function: MipsFunction) = also {
            this.functions[function.label] = function
        }

        fun setInitialCode(initialCode: MutableList<MipsInstruction>): Builder = also {
            this.initialCode = initialCode
        }

        fun addInitialCode(vararg instructions: MipsInstruction): Builder = also {
            this.initialCode.addAll(instructions)
        }

        fun build(config: MipsConfiguration): MipsProgram = MipsProgram(config, stringConstants, functions.values.toList(), initialCode)
    }
}