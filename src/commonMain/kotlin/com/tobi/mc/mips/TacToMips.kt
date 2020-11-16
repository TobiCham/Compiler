package com.tobi.mc.mips

import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.construct.TacFunction

class TacToMips(val config: MipsConfiguration) {

    fun toMips(program: TacProgram): MipsProgram = MipsProgram.Builder().also { builder ->
        addStrings(program.strings, builder)
        addFunction(program.code, builder)
    }.build(config)

    private fun addStrings(strings: Array<String>, builder: MipsProgram.Builder) {
        for ((i, string) in strings.withIndex()) {
            builder.addStringConstant("STRING$i", string)
        }
    }

    private fun addFunction(function: TacFunction, builder: MipsProgram.Builder) {
        val globalFunction = FunctionToMips.toMips(function.codeName, function, config, builder)

        builder.addInitialCode(MipsInstruction("main:"))
        builder.initialCode.addAll(FunctionToMips.getClosureCreationCode(config, globalFunction, 0))
        builder.addInitialCode(
            MipsInstruction("move", Register(config.argumentRegisters[0]), Register(config.resultRegister)),
            MipsInstruction("jal", Label(globalFunction.label))
        )
        for(variable in function.environment.newVariables.keys) {
            builder.addSystemFunction(getSystemFunction(variable))
        }
        builder.addSystemFunction(MipsHelperCode.CREATE_CLOSURE)
    }

    private fun getSystemFunction(name: String) = when(name) {
        "printInt" -> MipsGlobalFunctions.PRINT_INT
        "printString" -> MipsGlobalFunctions.PRINT_STRING
        "readInt" -> MipsGlobalFunctions.READ_INT
        "readString" -> MipsGlobalFunctions.READ_STRING
        "exit" -> MipsGlobalFunctions.EXIT
        "unixTime" -> MipsGlobalFunctions.UNIX_TIME
        else -> throw NotImplementedError("Function $name not yet implemented on MIPS")
    }
}