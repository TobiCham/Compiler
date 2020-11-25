package com.tobi.mc

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.util.TabbedBuilder

class ProgramToString(val styler: ProgramToStringStyler = Stylers.NONE) {

    fun toString(computable: Computable): String {
        val builder = TabbedBuilder()
        computable.toString(builder)
        return builder.toString()
    }

    fun Computable.toString(builder: TabbedBuilder): Unit = when(this) {
//        is InbuiltFunction -> builder.print("/* Compiled code */")
        is BreakStatement -> builder.print(styler.style(StyleType.BREAK, "break"))
        is ContinueStatement -> builder.print(styler.style(StyleType.CONTINUE, "continue"))
        is ReturnStatement -> {
            builder.print(styler.style(StyleType.RETURN, "return"))
            if(toReturn != null) {
                builder.print(" ")
                toReturn!!.toString(builder)
            }
            Unit
        }
        is IfStatement -> {
            builder.print(styler.style(StyleType.IF, "if"))
            builder.print(styler.style(StyleType.BRACKET, "("))

            check.toString(builder)
            builder.print(styler.style(StyleType.BRACKET, ")"))
            builder.print(" ")
            ifBody.toString(builder)

            if(elseBody != null) {
                builder.print(" ")
                builder.print(styler.style(StyleType.ELSE, "else"))
                builder.print(" ")

                val elseOps = elseBody!!.operations
                if(elseOps.size == 1 && elseOps[0] is IfStatement) {
                    elseOps[0].toString(builder)
                } else {
                    elseBody!!.toString(builder)
                }
            } else Unit
        }
        is WhileLoop -> {
            builder.print(styler.style(StyleType.WHILE, "while"))
            builder.print(styler.style(StyleType.BRACKET, "("))
            check.toString(builder)
            builder.print(styler.style(StyleType.BRACKET, ")"))
            builder.print(" ")
            body.toString(builder)
        }
        is FunctionDeclaration -> {
            builder.print(styler.style(StyleType.TYPE_DECLARATION, returnType?.toString() ?: "auto"))
            builder.print(' ')
            builder.print(styler.style(StyleType.NAME, name))
            builder.print(styler.style(StyleType.BRACKET, "("))
            builder.print(parameters.joinToString(styler.style(StyleType.PARAMS_SEPARATOR, ",") + " ") {
                "${styler.style(StyleType.TYPE_DECLARATION, it.type.toString())} ${styler.style(StyleType.NAME, it.name)}"
            })
            builder.print(styler.style(StyleType.BRACKET, ")"))
            builder.print(" ")
            body.toString(builder)
            builder.println("")
        }
        is VariableDeclaration -> {
            builder.print(styler.style(StyleType.TYPE_DECLARATION, expectedType?.toString() ?: "auto"))
            builder.print(" ")
            builder.print(styler.style(StyleType.NAME, name))
            builder.print(" ")
            builder.print(styler.style(StyleType.ASSIGNMENT, "="))
            builder.print(" ")
            value.toString(builder)
        }
        is SetVariable -> {
            builder.print(styler.style(StyleType.NAME, name))
            builder.print("<${this.contextIndex}>")
            builder.print(" ")
            builder.print(styler.style(StyleType.ASSIGNMENT, "="))
            builder.print(" ")
            value.toString(builder)
        }
        is GetVariable -> {
            builder.print(styler.style(StyleType.NAME, name))
            builder.print("<${this.contextIndex}>")
        }
        is FunctionCall -> {
            function.toString(builder)
            builder.print(styler.style(StyleType.BRACKET, "("))
            for((i, arg) in arguments.withIndex()) {
                if(i != 0) {
                    builder.print(styler.style(StyleType.PARAMS_SEPARATOR, ","))
                    builder.print(" ")
                }
                arg.toString(builder)
            }
            builder.print(styler.style(StyleType.BRACKET, ")"))
        }
        is MathOperation -> {
            arg1.toString(builder)
            builder.print(" ")
            builder.print(styler.style(StyleType.MATH, operationString))
            builder.print(" ")
            arg2.toString(builder)
        }
        is DataTypeInt -> builder.print(styler.style(StyleType.INT, this.toString()))
        is DataTypeString -> builder.print(styler.style(StyleType.STRING, this.toString()))
        is ExpressionSequence -> {
            builder.println(styler.style(StyleType.CURLY_BRACKET, "{"))
            builder.indent()
            for (operation in operations) {
                operation.toString(builder)

                if(operation.requiresSemiColon()) {
                    builder.print(styler.style(StyleType.SEMI_COLON, ";"))
                }
                builder.println("")
            }
            builder.outdent()
            builder.print(styler.style(StyleType.CURLY_BRACKET, "}"))
        }
        is UnaryMinus -> {
            builder.print(styler.style(StyleType.MATH, "-"))
            if(expression !is DataTypeInt) {
                builder.print(styler.style(StyleType.BRACKET, "("))
                expression.toString(builder)
                builder.print(styler.style(StyleType.BRACKET, ")"))
            } else {
                expression.toString(builder)
            }
        }
        is Negation -> {
            builder.print(styler.style(StyleType.NEGATION, "!"))
            if(negation !is DataTypeInt) {
                builder.print(styler.style(StyleType.BRACKET, "("))
                negation.toString(builder)
                builder.print(styler.style(StyleType.BRACKET, ")"))
            } else {
                negation.toString(builder)
            }
        }
        is StringConcat -> {
            str1.toString(builder)
            builder.print(" ")
            builder.print(styler.style(StyleType.STRING_CONCAT, "++"))
            builder.print(" ")
            str2.toString(builder)
        }
        is Program -> {
            for (operation in this.code.operations) {
                operation.toString(builder)
                if(operation.requiresSemiColon()) {
                    builder.print(';')
                }
                builder.println("")
            }
        }
        is FunctionPrototype -> {}
        else -> throw IllegalStateException("Unknown value $this")
    }

    private fun Computable.requiresSemiColon() = when(this) {
        is IfStatement -> false
        is WhileLoop -> false
        is FunctionDeclaration -> false
        else -> true
    }
}