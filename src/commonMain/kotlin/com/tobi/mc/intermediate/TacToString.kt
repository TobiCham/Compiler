package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.*
import com.tobi.mc.util.TabbedBuilder
import com.tobi.mc.util.escapeForPrinting

object TacToString {

    fun toString(tac: TacNode): String = TabbedBuilder().also { builder ->
        val labels = TacLabelCalculator.calculateLabels(tac)

        if(tac is TacProgram) {
            for(str in tac.strings) {
                builder.println("STRING \"${str.escapeForPrinting()}\"")
            }
            builder.println("")

            tac.mainFunction.environment.print(builder)
            tac.mainFunction.printVariables(builder)

            tac.mainFunction.code.print(builder, labels, HashSet())
        } else {
            tac.print(builder, labels, HashSet())
        }

    }.toString().trim()

    private fun TacNode.print(builder: TabbedBuilder, labels: TacLabels, generatedBlocks: MutableSet<TacBlock>) {
        when(this) {
            is TacBranchEqualZero -> {
                val successLabel =  labels.getLabel(this.successBlock)
                    ?: throw IllegalStateException("No label found for success block")

                builder.print("BrZ ")
                this.conditionVariable.print(builder, labels, generatedBlocks)
                builder.print(" ")
                builder.println(successLabel)

                if(generatedBlocks.contains(this.failBlock)) {
                    throw IllegalStateException("Fail block should not have already been generated")
                }
                if(this.failBlock.print(builder, labels, generatedBlocks)) {
                    builder.println("")
                }
                this.successBlock.print(builder, labels, generatedBlocks)
            }
            is TacBlock -> this.print(builder, labels, generatedBlocks)
            is TacFunctionCall -> {
                builder.print("Call ")
                this.function.print(builder, labels, generatedBlocks)
            }
            is TacGoto -> {
                if(generatedBlocks.contains(this.block)) {
                    val label = labels.getLabel(this.block)
                        ?: throw IllegalStateException("Block already generated but no label found")
                    builder.print("Goto $label")
                } else {
                    this.block.print(builder, labels, generatedBlocks)
                }
            }
            is TacFunction -> {
                builder.println("BeginFunc (${this.parameters}, ${this.variables.size}, ${this.calculateRegistersUsed()})")
                builder.indent()
                this.environment.print(builder)
                printVariables(builder)
                this.code.print(builder, labels, generatedBlocks)
                builder.println("")
                builder.outdent()
                builder.print("End")
            }
            is TacMathOperation -> {
                this.arg1.print(builder, labels, generatedBlocks)
                builder.print(" ${this.type.opString} ")
                this.arg2.print(builder, labels, generatedBlocks)
            }
            is TacUnaryMinus -> {
                builder.print("-")
                this.variable.print(builder, labels, generatedBlocks)
            }
            is TacNegation -> {
                builder.print("Negate ")
                this.toNegate.print(builder, labels, generatedBlocks)
            }
            is TacStringConcat -> {
                builder.print("Concat ")
                this.str1.print(builder, labels, generatedBlocks)
                builder.print(" ")
                this.str2.print(builder, labels, generatedBlocks)
            }
            is TacSetArgument -> {
                builder.print("SetArg ${this.index} ")
                this.variable.print(builder, labels, generatedBlocks)
            }
            is TacReturn -> builder.print("return")
            is TacSetVariable -> {
                this.variable.print(builder, labels, generatedBlocks)
                builder.print(" = ")
                this.value.print(builder, labels, generatedBlocks)
            }
            is RegisterVariable -> builder.print("r${this.register}")
            is StringVariable -> builder.print("STRING ${this.stringIndex}")
            is EnvironmentVariable -> builder.print("*(${this.name},${this.index})")
            is ReturnedValue -> builder.print("@return")
            is StackVariable -> builder.print(this.name)
            is IntValue -> builder.print(this.value)
            is ParamReference -> builder.print("Param ${this.index}")
            is TacInbuiltFunction -> builder.print("BeginFunc Inbuilt<${this.label}>")
            else -> throw IllegalArgumentException("Unknown tac construct ${this::class.simpleName}")
        }
    }

    private fun TacBlock.print(builder: TabbedBuilder, labels: TacLabels, generatedBlocks: MutableSet<TacBlock>): Boolean {
        if(!generatedBlocks.add(this)) {
            return false
        }
        val label = labels.getLabel(this)
        if (label != null) {
            builder.println(label + ":")
        }
        for ((i, instruction) in this.instructions.withIndex()) {
            instruction.print(builder, labels, generatedBlocks)
            if(i != this.instructions.size - 1) {
                builder.println("")
            }
        }
        return true
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