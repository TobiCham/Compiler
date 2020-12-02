package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.parser.util.traverseAllNodes
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object SingleFunctionCallOptimisation : InstanceOptimisation<ExpressionSequence>(ExpressionSequence::class) {

    private const val MAX_INLINES = 1

    override val description: DescriptionMeta = SimpleDescription("Single function call inliner", """
        Inlines the result of any function calls which only occur once when being returned
    """.trimIndent())

    override fun ExpressionSequence.optimiseInstance(): Computable? {
        val newSequence = ArrayList<Computable>()
        val inlines = ArrayList<InlineData>()

        for (operation in this.operations) {
            if(operation is FunctionDeclaration) {
                val inlineData = this.tryGetInlineData(operation) ?: continue
                inlines.add(inlineData)
            } else if(inlines.isEmpty()) {
                newSequence.add(operation)
            } else {
                newSequence.add(operation.mapInline(inlines))
            }
        }

        if(inlines.isEmpty()) {
            return null
        }
        return ExpressionSequence(newSequence)
    }

    private fun Computable.mapInline(inlines: List<InlineData>): Computable = when(this) {
        is ReturnStatement -> this.mapReturn(inlines)
        is ExpressionSequence -> ExpressionSequence(operations.map { it.mapInline(inlines) }, this.sourceRange)
        is IfStatement -> IfStatement(
            check,
            ifBody.mapInline(inlines) as ExpressionSequence,
            elseBody?.mapInline(inlines) as ExpressionSequence?,
            this.sourceRange
        )
        is WhileLoop -> WhileLoop(check, body.mapInline(inlines) as ExpressionSequence, this.sourceRange)
        else -> this
    }

    private fun ReturnStatement.mapReturn(inlines: List<InlineData>): Computable {
        val toReturn = this.toReturn
        if(toReturn !is FunctionCall) {
            return this
        }
        val inline = inlines.find { (_, returns) -> returns.any { it === this } } ?: return this
        return mapFunctionCall(toReturn, inline.function)
    }

    private fun mapFunctionCall(call: FunctionCall, functionCalled: FunctionDeclaration): ExpressionSequence {
        val argsExp = ArrayList<Computable>()
        for((i, arg) in call.arguments.withIndex()) {
            val parameter = functionCalled.parameters[i]

            //Increment variable indices as the arguments are nested by 1
            arg.traverseAllNodes().filterIsInstance<VariableContext>().forEach {
                it.contextIndex++
            }
            argsExp.add(VariableDeclaration(
                parameter.name, arg, parameter.type, arg.sourceRange
            ))
        }
        argsExp.add(functionCalled.body)
        argsExp.add(ReturnStatement(null, call.sourceRange))

        return ExpressionSequence(argsExp, call.sourceRange)
    }

    private fun ExpressionSequence.tryGetInlineData(function: FunctionDeclaration): InlineData? {
        val returnInvocations: MutableList<ReturnStatement> = ArrayList()
        var getters = 0
        var setters = 0

        fun isValidContext(data: Pair<Computable, Int>): Boolean {
            val (computable, depth) = data
            return computable is VariableContext && computable.name == function.name && computable.contextIndex == depth - 1
        }

        this.traverseWithDepth().mapNotNull { data ->
            when {
                data.first is ReturnStatement -> data
                isValidContext(data) -> data
                else -> null
            }
        }.forEach { (computable, depth) ->
            when(computable) {
                is SetVariable -> setters++
                is GetVariable -> getters++
                is VariableContext -> throw ParseException("Invalid variable", computable)
                is ReturnStatement -> {
                    val toReturn = computable.toReturn
                    if(toReturn is FunctionCall) {
                        if(toReturn.function is GetVariable && isValidContext(Pair(toReturn.function, depth))) {
                            returnInvocations.add(computable)
                        }
                    }
                }
            }
        }

        if(getters == 1 && setters == 0 && returnInvocations.size <= MAX_INLINES && returnInvocations.isNotEmpty() && !isVariableInClosure(function.name)) {
            return InlineData(function, returnInvocations)
        }
        return null
    }

    private data class InlineData(val function: FunctionDeclaration, val returns: List<ReturnStatement>)

    private fun ExpressionSequence.isVariableInClosure(name: String): Boolean = operations.any {
        it.findVariable(name, 0, false) != null
    }

    private fun Computable.findVariable(name: String, currentDepth: Int, isInFunction: Boolean): Computable? = when(this) {
        is VariableContext -> if(this.contextIndex == currentDepth && isInFunction && this.name == name) this else null
        is ExpressionSequence -> this.operations.asSequence().map {
            it.findVariable(name, currentDepth + 1, isInFunction)
        }.filterNotNull().firstOrNull()
        is FunctionDeclaration -> this.body.findVariable(name, currentDepth + 1, true)
        else -> getComponents().asSequence().map {
            it.findVariable(name, currentDepth, isInFunction)
        }.filterNotNull().firstOrNull()
    }


}