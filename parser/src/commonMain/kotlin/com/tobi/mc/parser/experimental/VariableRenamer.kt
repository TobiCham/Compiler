package com.tobi.mc.parser.experimental

import com.tobi.mc.computable.*
import com.tobi.mc.parser.util.getComponents

internal object VariableRenamer {

    fun renameVariable(computable: Computable, from: String, to: String) {
        return computable.rename(from, to)
    }
    
    fun renameFunctionArgument(function: FunctionDeclaration, from: String, to: String) {
        function.parameters = function.parameters.map {
            if(it.name == from) Parameter(it.type, to) else it
        }
        function.body.rename(from, to)
    }

    private fun <T : Computable> T.rename(from: String, to: String) {
        when(this) {
            is Data -> return
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
                var ignore = false

                for(operation in operations) {
                    if(!ignore) operation.rename(from, to)

                    //If 'from' is being redefined, nothing below this statement will need to be redefined
                    if(operation is DefineVariable && operation.name == from) ignore = true
                    if(operation is FunctionDeclaration && operation.name == from) ignore = true
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
            is ReturnExpression -> {
                toReturn?.rename(from, to)
            }
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