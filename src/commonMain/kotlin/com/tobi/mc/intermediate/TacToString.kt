package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.util.TabbedBuilder
import com.tobi.mc.util.escapeForPrinting

object TacToString {

    fun toString(program: TacProgram): String = TabbedBuilder().also { builder ->
        for(str in program.strings) {
            builder.println("STRING \"${str.escapeForPrinting()}\"")
        }
        builder.println("")

        program.code.environment.print(builder)
        program.code.printVariables(builder)

        for(line in program.code.code) {
            line.print(builder)
            builder.println("")
        }
    }.toString().trim()

    private fun TacStructure.print(builder: TabbedBuilder): Any = when(this) {
        is TacFunction -> this.print(builder)
        is TacBranchEqualZero -> {
            builder.print("BrZ ")
            this.conditionVariable.print(builder)
            builder.print(" ${this.branchTo}")
        }
        is TacFunctionCall -> {
            builder.print("Call ")
            this.function.print(builder)
        }
        is TacGoto -> builder.print("Goto ${this.label}")
        is TacLabel -> builder.print("${this.label}:")
        is TacMathOperation -> {
            this.arg1.print(builder)
            builder.print(" ${this.type.opString} ")
            this.arg2.print(builder)
        }
        is TacUnaryMinus -> {
            builder.print("-")
            this.variable.print(builder)
        }
        is TacNegation -> {
            builder.print("Negate ")
            this.toNegate.print(builder)
        }
        is TacStringConcat -> {
            builder.print("Concat ")
            this.str1.print(builder)
            builder.print(" ")
            this.str2.print(builder)
        }
        is TacSetArgument -> {
            builder.print("SetArg ${this.index} ")
            this.variable.print(builder)
        }
        is TacReturn -> builder.print("return")
        is TacSetVariable -> {
            this.variable.print(builder)
            builder.print(" = ")
            this.value.print(builder)
        }
        is RegisterVariable -> builder.print("r${this.register}")
        is StringVariable -> builder.print("STRING ${this.stringIndex}")
        is EnvironmentVariable -> builder.print("*(${this.name},${this.index})")
        is ReturnedValue -> builder.print("@return")
        is StackVariable -> builder.print(this.name)
        is IntValue -> builder.print(this.value)
        is ParamReference -> builder.print("GetParam ${this.index}")
        is TacInbuiltFunction -> builder.print("BeginFunc Inbuilt<${this.label}>")
        else -> throw IllegalArgumentException("Unknown tac construct ${this::class.simpleName}")
    }

    private fun TacEnvironment.print(builder: TabbedBuilder) {
        val vars = this.newVariables
        if(!vars.isEmpty()) {
            builder.println("Env")
            builder.indent()
            vars.forEach(builder::println)
            builder.outdent()
            builder.println("End")
        }
    }

    private fun TacFunction.print(builder: TabbedBuilder) {
        builder.println("BeginFunc (${this.parameters}, ${this.variables.size}, ${this.registersUsed})")
        builder.indent()
        this.environment.print(builder)
        printVariables(builder)

        for(line in this.code) {
            line.print(builder)
            builder.println("")
        }
        builder.outdent()
        builder.print("End")
    }

    private fun TacFunction.printVariables(builder: TabbedBuilder) {
        if(this.variables.isNotEmpty()) {
            builder.println("Vars")
            builder.indent()
            this.variables.forEach(builder::println)
            builder.outdent()
            builder.println("End")
        }
    }
}