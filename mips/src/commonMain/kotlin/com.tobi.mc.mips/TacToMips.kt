package com.tobi.mc.mips

import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.construct.TacFunction

class TacToMips(val config: MipsConfiguration) {

    fun toMips(program: TacProgram): MipsProgram = MipsProgram.Builder().also { builder ->
        addStrings(program.strings, builder)
        addFunctions(program.functions, builder)
    }.build(config)

    private fun addStrings(strings: Array<String>, builder: MipsProgram.Builder) {
        for ((i, string) in strings.withIndex()) {
            builder.addStringConstant("STRING$i", string)
        }
    }

    private fun addFunctions(functions: List<TacFunction>, builder: MipsProgram.Builder) {
        for (function in functions) {
            builder.addFunction(FunctionToMips.toMips(function, config))
        }
    }
}