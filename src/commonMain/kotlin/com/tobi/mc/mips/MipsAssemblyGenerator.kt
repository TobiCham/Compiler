package com.tobi.mc.mips

import com.tobi.mc.util.TabbedBuilder
import com.tobi.mc.util.escapeForPrinting

object MipsAssemblyGenerator {

    fun generateAssembly(program: MipsProgram, builder: TabbedBuilder = TabbedBuilder()): String {
        val stringConstants = program.stringConstants
        if(stringConstants.isNotEmpty()) {
            builder.println(".data")
            for((name, value) in stringConstants) {
                builder.indent()
                builder.println("$name: .asciiz \"${value.escapeForPrinting()}\"")
                builder.outdent()
            }
            builder.println("")
        }

        builder.println(".text")
        for(instruction in program.initialCode) {
            builder.println(program.config.formatInstruction(instruction))
        }
        builder.println("")

        val functions = program.functions
        for(function in functions) {
            builder.println("${function.label}:")
            builder.indent()
            for(instruction in function.instructions) {
                builder.println(program.config.formatInstruction(instruction))
            }
            builder.outdent()
            builder.println("")
        }

        for(function in program.inbuiltFunctions) {
            builder.println("")
            builder.println(function)
        }

        return builder.toString()
    }
}