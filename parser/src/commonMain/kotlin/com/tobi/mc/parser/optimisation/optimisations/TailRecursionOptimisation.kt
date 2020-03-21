package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.traverseAllNodes
import com.tobi.mc.util.DescriptionMeta

internal object TailRecursionOptimisation : InstanceOptimisation<FunctionDeclaration>(FunctionDeclaration::class) {

    override val description: DescriptionMeta = SimpleDescription("Tail-recursion reduction", """
        Converts tail recursive functions into iterative functions
    """.trimIndent())

    override fun FunctionDeclaration.optimise(replace: (Computable) -> Boolean): Boolean {
        if(!this.isTailRecursive()) {
            return false
        }

        val names = HashSet<String>()
        for(component in traverseAllNodes()) {
            if(component is FunctionDeclaration) {
                component.parameters.forEach { names.add(it.first) }
            }
            if(component is VariableReference) {
                names.add(component.name)
            }
        }
        val paramNames = HashMap<String, String>()
        for((name, _) in parameters) {
            paramNames[name] = findVariableName("tail_$name", names)
        }

        val newBody = ExpressionSequence(listOf(
            WhileLoop(DataTypeInt(1), body.convertToTailrec(this, paramNames) as ExpressionSequence)
        ))
        return replace(FunctionDeclaration(name, parameters, newBody, returnType))
    }

    private fun findVariableName(preferredName: String, names: MutableSet<String>): String {
        if(!names.contains(preferredName)) {
            names.add(preferredName)
            return preferredName
        }
        var counter = 0
        while(true) {
            val name = "$preferredName$counter"
            if(!names.contains(name)) {
                names.add(name)
                return name
            }
            counter++
        }
    }

    private fun Computable.convertToTailrec(tailRecFunc: FunctionDeclaration, tempParamNames: Map<String, String>): Computable = when(this) {
        is ReturnExpression -> this.convertToTailrec(tailRecFunc, tempParamNames)
        is ExpressionSequence -> ExpressionSequence(operations.map { it.convertToTailrec(tailRecFunc, tempParamNames) })
        is IfStatement -> IfStatement(check, ifBody.convertToTailrec(tailRecFunc, tempParamNames) as ExpressionSequence,
            elseBody?.convertToTailrec(tailRecFunc, tempParamNames) as ExpressionSequence?
        )
        is WhileLoop -> WhileLoop(check, body.convertToTailrec(tailRecFunc, tempParamNames) as ExpressionSequence)
        else -> this
    }

    private fun ReturnExpression.convertToTailrec(tailRecFunc: FunctionDeclaration, tempParamNames: Map<String, String>): Computable {
        if(!this.toReturn.isTailRecursiveCall(tailRecFunc.name)) {
            return this
        }
        val list = ArrayList<Computable>()
        for((i, value) in tailRecFunc.parameters.withIndex()) {
            list.add(DefineVariable(tempParamNames[value.first]!!, (this.toReturn as FunctionCall).arguments[i], value.second))
        }
        for((name, _) in tailRecFunc.parameters) {
            list.add(SetVariable(name, GetVariable(tempParamNames[name]!!)))
        }
        list.add(ContinueStatement)
        return ExpressionSequence(list)
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
            it is ReturnExpression && it.toReturn.isTailRecursiveCall(this.name)
        }
    }

    private fun Computable.violatesTailRec(mainFunc: FunctionDeclaration): Boolean {
        if(this is FunctionDeclaration) {
            if(this.parameters.any { it.first == mainFunc.name }) {
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
}