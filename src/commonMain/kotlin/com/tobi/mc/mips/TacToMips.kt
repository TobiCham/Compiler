package com.tobi.mc.mips

import com.tobi.mc.intermediate.TacLabelCalculator
import com.tobi.mc.intermediate.TacLabels
import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.TacReturnMerger
import com.tobi.mc.intermediate.code.TacFunction
import com.tobi.mc.intermediate.optimisation.Optimisations
import com.tobi.mc.intermediate.optimisation.TacOptimiser

class TacToMips(val config: MipsConfiguration) {

    fun toMips(program: TacProgram): MipsProgram {
        //Optimisation to ensure that there aren't multiple lines of the same return code
        TacReturnMerger.mergeReturns(program)
        TacOptimiser(Optimisations.ALL_OPTIMISATIONS).optimise(program)

        val builder = MipsProgram.Builder()
        addStrings(program.strings, builder)
        addFunction(program.mainFunction, TacLabelCalculator.calculateLabels(program), builder)

        return builder.build(config)
    }

    private fun addStrings(strings: Array<String>, builder: MipsProgram.Builder) {
        for ((i, string) in strings.withIndex()) {
            builder.addStringConstant("STRING$i", string)
        }
    }

    private fun addFunction(function: TacFunction, labels: TacLabels, builder: MipsProgram.Builder) {
        val globalFunction = FunctionToMips.toMips(function, config, labels, builder)

        builder.addInitialCode(MipsInstruction("main:"))
        builder.initialCode.addAll(FunctionToMips.getClosureCreationCode(config, globalFunction))
        builder.addInitialCode(
            MipsInstruction("move", Register(config.argumentRegisters[0]), Register(config.resultRegister)),
            MipsInstruction("jal", Label(globalFunction.label))
        )
        for(variable in function.environment.newVariables) {
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