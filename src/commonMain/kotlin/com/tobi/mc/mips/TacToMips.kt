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

        val newInstructions = ArrayList<MipsInstruction>()
        for((i, variable) in function.environment.newVariables.keys.withIndex()) {
            when(variable) {
                "printInt" -> handleSystemFunction(i, MipsGlobalFunctions.PRINT_INT, variable, newInstructions, builder)
                "printString" -> handleSystemFunction(i, MipsGlobalFunctions.PRINT_STRING, variable, newInstructions, builder)
                "readInt" -> handleSystemFunction(i, MipsGlobalFunctions.READ_INT, variable, newInstructions, builder)
                "readString" -> handleSystemFunction(i, MipsGlobalFunctions.READ_STRING, variable, newInstructions, builder)
                "exit" -> handleSystemFunction(i, MipsGlobalFunctions.EXIT, variable, newInstructions, builder)
                "unixTime" -> handleSystemFunction(i, MipsGlobalFunctions.UNIX_TIME, variable, newInstructions, builder)
                else -> throw IllegalArgumentException("Unknown variable ${variable}")
            }
        }
        newInstructions.addAll(globalFunction.instructions)
        globalFunction.instructions = newInstructions

        builder.addInitialCode(
            MipsInstruction("move", MipsArgument.Register(config.framePointer), MipsArgument.Register(config.stackPointer)),
        )
        builder.initialCode.addAll(getClosureInstructions(globalFunction.stackVariables, globalFunction.registers, globalFunction.environmentVariables, globalFunction.label))
        builder.addInitialCode(
            MipsInstruction("move", MipsArgument.Register(config.closureRegister), MipsArgument.Register(config.resultRegister)),
            MipsInstruction("move", MipsArgument.Register(config.argumentRegisters[0]), MipsArgument.Register(config.resultRegister)),
            MipsInstruction("jal", MipsArgument.Label(FunctionToMips.PROCEDURE_CALL_CLOSURE))
        )
    }

    private fun handleSystemFunction(index: Int, code: String, name: String, instructions: MutableList<MipsInstruction>, builder: MipsProgram.Builder) {
        val func = createSystemFunction(code, name, builder)

        instructions.addAll(getClosureInstructions(func.stackVariables, func.registers, func.environmentVariables, name))
        instructions.add(MipsInstruction("lw", MipsArgument.Register(config.argumentRegisters[0]), MipsArgument.IndirectRegister(config.currentEnvironmentRegister, 0)))
        instructions.add(MipsInstruction("sw", MipsArgument.Register(config.resultRegister), MipsArgument.IndirectRegister(config.argumentRegisters[0], index * 4)))
    }

    private fun getClosureInstructions(stackVars: Int, registerVars: Int, envVars: Int, label: String): List<MipsInstruction> {
        val instructions = ArrayList<MipsInstruction>()
        instructions.add(MipsInstruction("la", MipsArgument.Register(config.argumentRegisters[0]), MipsArgument.Label(label)))
        instructions.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[1]), MipsArgument.Value(stackVars)))
        instructions.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[2]), MipsArgument.Value(registerVars)))
        instructions.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[3]), MipsArgument.Value(envVars)))
        instructions.add(MipsInstruction("jal", MipsArgument.Label(FunctionToMips.PROCEDURE_CREATE_CLOSURE)))
        return instructions
    }

    private fun createSystemFunction(code: String, name: String, builder: MipsProgram.Builder): MipsFunction {
        val function = MipsFunction(name, listOf(MipsInstruction(code.trim())), 0, 0, 0)
        builder.addFunction(function)
        return function
    }
}