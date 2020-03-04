package com.tobi.mc

import com.tobi.mc.computable.*
import com.tobi.mc.inbuilt.InbuiltFunction
import com.tobi.util.TabbedBuilder

object ProgramToString {

    fun toString(program: Program): String {
        val builder = TabbedBuilder()
        for(func in program) {
            func.toString(builder)
            builder.println("")
        }
        return builder.toString()
    }

    fun toString(computable: Computable): String {
        val builder = TabbedBuilder()
        computable.toString(builder)
        return builder.toString()
    }


    fun Computable.toString(builder: TabbedBuilder): Unit = when(this) {
        is InbuiltFunction -> builder.print("/* Compiled code */")
        is BreakStatement -> builder.print("break")
        is ContinueStatement -> builder.print("continue")
        is ReturnExpression -> {
            if(toReturn == null) builder.print("return")
            else {
                builder.print("return ")
                toReturn!!.toString(builder)
            }
        }
        is IfStatement -> {
            builder.print("if(")
            check.toString(builder)
            builder.print(") ")
            ifBody.toString(builder)

            if(elseBody != null) {
                builder.print(" else ")
                elseBody!!.toString(builder)
            } else Unit
        }
        is WhileLoop -> {
            builder.print("while(")
            check.toString(builder)
            builder.print(") ")
            body.toString(builder)
        }
        is FunctionDeclaration -> {
            builder.print(returnType ?: "auto")
            builder.print(' ')
            builder.print(name)
            builder.print('(')
            builder.print(parameters.joinToString(", ") { "${it.second} ${it.first}" })
            builder.print(") ")
            body.toString(builder)
        }
        is DefineVariable -> {
            builder.print(expectedType ?: "auto")
            builder.print(" $name = ")
            value.toString(builder)
        }
        is SetVariable -> {
            builder.print("$name = ")
            value.toString(builder)
        }
        is GetVariable -> builder.print(name)
        is FunctionCall -> {
            function.toString(builder)
            builder.print('(')
            for((i, arg) in arguments.withIndex()) {
                if(i != 0) builder.print(", ")
                arg.toString(builder)
            }
            builder.print(')')
        }
        is MathOperation -> {
            arg1.toString(builder)
            builder.print(" $operationString ")
            arg2.toString(builder)
        }
        is Data -> builder.print(toString())
        is ExpressionSequence -> {
            builder.println('{')
            builder.indent()
            for (operation in operations) {
                operation.toString(builder)

                if(operation.requiresSemiColon()) {
                    builder.print(";")
                }
                builder.println("")
            }
            builder.outdent()
            builder.print('}')
        }
        else -> throw IllegalStateException("Unknown value $this")
    }

    private fun Computable.requiresSemiColon() = when(this) {
        is IfStatement -> false
        is WhileLoop -> false
        is FunctionDeclaration -> false
        else -> true
    }
}