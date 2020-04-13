package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.util.TabbedBuilder
import com.tobi.mc.util.escapeForPrinting

object TacToString {

    fun toString(program: TacProgram): String = TabbedBuilder().also { builder ->
        for(str in program.strings) {
            builder.println("STRING \"${str.escapeForPrinting()}\"")
        }
        builder.println("")

        for(env in program.environments) {
            env.print(builder)
        }
        for(function in program.functions) {
            function.print(builder)
        }
    }.toString().trim()

    private fun TacEnvironment.print(builder: TabbedBuilder) {
        builder.println("Env ${this.name} ${this.parent!!.name}")
        builder.indent()
        for((name, type) in this.variables) {
            builder.println("$type $name")
        }
        builder.outdent()
        builder.println("End")
        builder.println("")
    }

    private fun TacFunction.print(builder: TabbedBuilder) {
        builder.println("BeginFunc ${this.name}")
        builder.indent()
        for(line in this.code) {
            line.print(builder)
            builder.println("")
        }
        builder.outdent()
        builder.println("End")
        builder.println("")
    }

    private fun TacCodeConstruct.print(builder: TabbedBuilder): Any = when(this) {
        is ConstructBranchZero -> {
            builder.print("BrZ ")
            this.conditionVariable.print(builder)
            builder.print(" ${this.branchLabel}")
        }
        is ConstructCreateEnvironment -> builder.print("make ${this.environment.name}")
        is ConstructFunctionCall -> {
            builder.print("Call ")
            this.function.print(builder)
        }
        is ConstructGoto -> builder.print("Goto ${this.label}")
        is ConstructLabel -> builder.print("${this.label}:")
        is ConstructMath -> {
            this.arg1.print(builder)
            builder.print(" ${this.type.opString} ")
            this.arg2.print(builder)
        }
        is ConstructNegation -> {
            builder.print("Negate ")
            this.toNegate.print(builder)
        }
        is ConstructStringConcat -> {
            builder.print("Concat ")
            this.str1.print(builder)
            builder.print(" ")
            this.str2.print(builder)
        }
        is ConstructPushArgument -> {
            builder.print("PushArg ")
            this.variable.print(builder)
        }
        is ConstructPopVariable -> builder.print("PopArg")
        is ConstructReturn -> {
            builder.print("return")
            if(this.toReturn != null) {
                builder.print(" ")
                this.toReturn.print(builder)
            } else {}
        }
        is ConstructSetVariable -> {
            (this.variable as TacCodeConstruct).print(builder)
            builder.print(" = ")
            this.value.print(builder)
        }
        is RegisterVariable -> builder.print("r${this.register}")
        is StringVariable -> builder.print("STRING ${this.stringIndex}")
        is EnvironmentVariable -> builder.print(this.name)
        is IntValue -> builder.print(this.value)
        is ParamReference -> builder.print("GetParam ${this.index}")
        else -> throw IllegalArgumentException("Unknown tac construct ${this::class.simpleName}")
    }
}