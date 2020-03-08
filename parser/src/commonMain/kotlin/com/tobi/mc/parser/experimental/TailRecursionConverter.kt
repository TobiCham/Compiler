package com.tobi.mc.parser.experimental

import com.tobi.mc.ProgramToString
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.syntax.VariablesState

object TailRecursionConverter {

    fun convert(function: FunctionDeclaration): FunctionDeclaration {
        val newBody = ExpressionSequence(listOf(
            WhileLoop(DataTypeInt(1), function.body.convertToTailrec(function) as ExpressionSequence)
        ))
        return FunctionDeclaration(function.name, function.parameters, newBody, function.returnType)
    }

    private fun Computable.convertToTailrec(tailRecFunc: FunctionDeclaration): Computable = when(this) {
        is ReturnExpression -> {
            if(this.toReturn is FunctionCall && (this.toReturn as FunctionCall).function is GetVariable && ((this.toReturn as FunctionCall).function as GetVariable).name == tailRecFunc.name) {
                val list = ArrayList<Computable>()
                for((paramName, type) in tailRecFunc.parameters) {
                    list.add(DefineVariable("old_$paramName", GetVariable(paramName), type))
                }

                for((i, value) in tailRecFunc.parameters.withIndex()) {
                    var newValue = (this.toReturn as FunctionCall).arguments[i]
                    println(ProgramToString().toString(newValue))
                    for((name, _) in tailRecFunc.parameters) {
                        newValue = VariableRenamer.renameVariable(newValue, name, "old_$name") as DataComputable
                        println(ProgramToString().toString(newValue))
                    }
                    println()
                    list.add(SetVariable(value.first, newValue))
                }
                list.add(ContinueStatement)
                ExpressionSequence(list)
            } else {
                this
            }
        }
        is ExpressionSequence -> ExpressionSequence(operations.map { it.convertToTailrec(tailRecFunc) })
        is IfStatement -> IfStatement(check, ifBody.convertToTailrec(tailRecFunc) as ExpressionSequence,
            elseBody?.convertToTailrec(tailRecFunc) as ExpressionSequence?
        )
        is WhileLoop -> WhileLoop(check, body.convertToTailrec(tailRecFunc) as ExpressionSequence)
        else -> this
    }

    private fun Computable.isVariableRedefined(name: String, state: VariablesState) = when(this) {
        is SetVariable -> this.name == name
        is DefineVariable -> this.name == name
        is FunctionDeclaration -> this.name == name
        else -> false
    }
}