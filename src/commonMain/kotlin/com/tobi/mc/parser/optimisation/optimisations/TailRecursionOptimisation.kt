package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.ContinueStatement
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.Parameter
import com.tobi.mc.computable.variable.*
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.traverseAllNodes
import com.tobi.mc.parser.util.traverseWithDepth
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object TailRecursionOptimisation : InstanceOptimisation<FunctionDeclaration>(FunctionDeclaration::class) {

    override val description: DescriptionMeta = SimpleDescription("Tail-recursion reduction", """
        Converts tail recursive functions into iterative functions
    """.trimIndent())

    override fun FunctionDeclaration.optimiseInstance(): Computable? {
        if(!this.isTailRecursive()) {
            return null
        }

        val loopBody = body.convertToTailrec(this, 0) as ExpressionSequence
        loopBody.incrementVariableContexts()

        val newBody = ExpressionSequence(listOf(
            WhileLoop(DataTypeInt(1), loopBody, this.body.sourceRange)
        ), this.body.sourceRange)
        return FunctionDeclaration(name, parameters, newBody, returnType, this.sourceRange)
    }

    private fun Computable.convertToTailrec(tailRecFunc: FunctionDeclaration, parameterDepth: Int): Computable = when(this) {
        is ReturnStatement -> this.convertReturnToTailrec(tailRecFunc, parameterDepth)
        is ExpressionSequence -> ExpressionSequence(operations.map { it.convertToTailrec(tailRecFunc, parameterDepth + 1) }, this.sourceRange)
        is IfStatement -> IfStatement(
            check,
            ifBody.convertToTailrec(tailRecFunc, parameterDepth) as ExpressionSequence,
            elseBody?.convertToTailrec(tailRecFunc, parameterDepth) as ExpressionSequence?,
            this.sourceRange
        )
        is WhileLoop -> WhileLoop(check, body.convertToTailrec(tailRecFunc, parameterDepth) as ExpressionSequence, this.sourceRange)
        else -> this
    }

    private fun ReturnStatement.convertReturnToTailrec(tailRecFunc: FunctionDeclaration, parameterDepth: Int): Computable {
        if(!this.toReturn.isTailRecursiveCall(tailRecFunc.name)) {
            return this
        }
        val list = ArrayList<Computable>()
        val paramNames = HashSet(tailRecFunc.parameters.map(Parameter::name))
        val paramNameMapping = HashMap<String, String>()

        for((i, parameter) in tailRecFunc.parameters.withIndex()) {
            val mappingName = findNewName(paramNames, "tail_" + parameter.name)
            paramNames.add(mappingName)
            paramNameMapping[parameter.name] = mappingName

            val argument = (this.toReturn as FunctionCall).arguments[i]
            ExpressionSequence(listOf(argument)).incrementVariableContexts()
            list.add(VariableDeclaration(mappingName, argument, parameter.type, argument.sourceRange))
        }
        for((i, param) in tailRecFunc.parameters.withIndex()) {
            val name = param.name
            val arg = (this.toReturn as FunctionCall).arguments[i]
            list.add(SetVariable(name, parameterDepth + 1, GetVariable(paramNameMapping[name]!!, 0, arg.sourceRange), arg.sourceRange))
        }
        //No source mapping for the "continue", so just use the return statement
        list.add(ContinueStatement(this.sourceRange))
        return ExpressionSequence(list, this.sourceRange)
    }

    private fun Computable?.isTailRecursiveCall(functionName: String): Boolean {
        if(this !is FunctionCall) return false
        val func = function
        if(func !is GetVariable) return false
        return func.name == functionName
    }

    private fun FunctionDeclaration.isTailRecursive(): Boolean {
        if(this.traverseAllNodes().any { it.violatesTailRec(this) }) {
            return false
        }
        return this.traverseAllNodes().any {
            it is ReturnStatement && it.toReturn.isTailRecursiveCall(this.name)
        }
    }

    private fun Computable.violatesTailRec(mainFunc: FunctionDeclaration): Boolean {
        if(this is FunctionDeclaration) {
            if(this.parameters.any { it.name == mainFunc.name }) {
                return true
            }
            if(this === mainFunc) {
                return false
            }
        }
        if(this is VariableReference && this !is GetVariable) {
            if(this.name == mainFunc.name) {
                return true
            }
        }
        return false
    }

    private fun ExpressionSequence.incrementVariableContexts() {
        for((component, depth) in this.traverseWithDepth()) {
            if(component is VariableContext && component.contextIndex >= depth) {
                component.contextIndex++
            }
        }
    }

    private fun findNewName(used: Set<String>, name: String): String {
        var counter = 0
        var current = name
        while(used.contains(current)) {
            current = name + counter.toString()
            counter++
        }
        return current
    }
}