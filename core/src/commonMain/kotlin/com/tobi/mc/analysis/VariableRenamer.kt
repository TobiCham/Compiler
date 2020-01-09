package com.tobi.mc.analysis

import com.tobi.mc.Data
import com.tobi.mc.computable.*
import com.tobi.mc.inbuilt.InbuiltFunction

object VariableRenamer {

    fun renameVariable(computable: Computable, from: String, to: String): Computable {
        return computable.rename(from, to)
    }
    
    fun renameFunctionArgument(function: FunctionDeclaration, from: String, to: String): FunctionDeclaration {
        val newBody = function.body.rename(from, to) as ExpressionSequence
        val newParams = function.parameters.map {
            if(it.first == from) Pair(to, it.second)
            else it
        }
        return FunctionDeclaration(function.name, newParams, newBody, function.returnType)
    }

    private fun Computable.rename(from: String, to: String): Computable = when(this) {
        is InbuiltFunction, BreakStatement, ContinueStatement -> this
        is Data -> this
        is SetVariable -> {
            val newName = getNewName(name, from, to)
            SetVariable(newName, value.rename(from, to) as DataComputable)
        }
        is GetVariable -> GetVariable(getNewName(name, from, to))
        is DefineVariable -> DefineVariable(name, value.rename(from, to) as DataComputable, expectedType)
        is FunctionDeclaration -> {
            if(name == from || parameters.any { it.first == from }) this
            else FunctionDeclaration(name, parameters, body.rename(from, to) as ExpressionSequence, returnType)
        }
        is ExpressionSequence -> {
            val newOperations = ArrayList<Computable>()
            var ignore = false

            for(operation in operations) {
                newOperations.add(if(ignore) operation else operation.rename(from, to))
                if(operation is DefineVariable && operation.name == from) ignore = true
                if(operation is FunctionDeclaration && operation.name == from) ignore = true
            }
            ExpressionSequence(newOperations)
        }
        is IfStatement -> IfStatement(
            check.rename(from, to) as DataComputable,
            ifBody.rename(from, to) as ExpressionSequence,
            elseBody?.rename(from, to) as ExpressionSequence?
        )
        is WhileLoop -> WhileLoop(
            check.rename(from, to) as DataComputable,
            body.rename(from, to) as ExpressionSequence
        )
        is FunctionCall -> FunctionCall(
            function.rename(from, to) as DataComputable,
            arguments.map {
                it.rename(from, to) as DataComputable
            }
        )
        is MathOperation -> MathOperation(
            arg1.rename(from, to),
            arg2.rename(from, to),
            operationString,
            computation
        )
        is ReturnExpression -> ReturnExpression(toReturn?.rename(from, to))
        else -> throw RuntimeException("Unknown computable $this")
    }

    private fun getNewName(name: String, from: String, to: String): String {
        return if(name == from) to else name
    }
}