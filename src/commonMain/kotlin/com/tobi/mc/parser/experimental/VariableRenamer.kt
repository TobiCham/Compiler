package com.tobi.mc.parser.experimental

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.VariableReference
import com.tobi.mc.parser.util.getComponents

object VariableRenamer {

    fun renameVariable(computable: Computable, from: String, to: String) {
        return computable.rename(from, to)
    }

    private fun <T : Computable> T.rename(from: String, to: String) {
        when(this) {
            is DefineVariable -> {
                if(this.name == to) {
                    throw IllegalStateException("Conflict - variable declaration already uses variable '$to'")
                }
            }
            is VariableReference -> this.name = getNewName(this.name, from, to)
            is FunctionDeclaration -> {
                if(name == to || parameters.any { it.name == to }) {
                    throw IllegalStateException("Conflict - function already uses variable '$to'")
                }

                if(name == from || parameters.any { it.name == from }) {
                    //'from' is being redefined, nothing left to do
                    return
                }
            }
            is ExpressionSequence -> {
                for(operation in operations) {
                    operation.rename(from, to)

                    //If 'from' is being redefined, nothing below this statement will need to be redefined
                    if(operation is DefineVariable && operation.name == from) break
                    if(operation is FunctionDeclaration && operation.name == from) break
                }
                //Cancel normal children renaming
                return
            }
            is IfStatement -> {
                check.rename(from, to)
                ifBody.rename(from, to)
                elseBody?.rename(from, to)
            }
            is WhileLoop -> {
                check.rename(from, to)
                body.rename(from, to)
            }
            is FunctionCall -> {
                function.rename(from, to)
                arguments.forEach {
                    it.rename(from, to)
                }
            }
            is MathOperation -> {
                arg1.rename(from, to)
                arg2.rename(from, to)
            }
            is ReturnStatement -> {
                toReturn?.rename(from, to)
            }
            is StringConcat -> {
                str1.rename(from, to)
                str2.rename(from, to)
            }
            is Negation -> negation.rename(from, to)
            else -> throw RuntimeException("Unknown computable $this")
        }
        for(component in this.getComponents()) {
            component.rename(from, to)
        }
    }

    private fun getNewName(name: String, from: String, to: String): String {
        return if(name == from) to else name
    }
}