package com.tobi.mc.parser.syntax.types

import com.tobi.mc.Data
import com.tobi.mc.DefaultContext
import com.tobi.mc.analysis.*
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.inbuilt.InbuiltFunction
import com.tobi.mc.parser.ParseException
import com.tobi.util.orElse

object TypeDetection {

    fun checkAndDetectTypes(program: Program, defaultContext: DefaultContext) {
        val parentState = VariableTypeState(null)

        for(variable in defaultContext.defaultVariables) {
            parentState.define(variable.name, variable.expandedType)

            if(variable is InbuiltFunction) {
                parentState.defineFunctionParams(variable.name, variable.params)
            }
        }
        val stateToUse = VariableTypeState(parentState)
        for(func in program) {
            func.detectTypes(stateToUse, null)
        }
    }

    private fun Computable.detectTypes(state: VariableTypeState, currentFunction: FunctionTypeData?) {
        var newState = state
        when(this) {
            is SetVariable -> this.handle(state)
            is DefineVariable -> this.handle(state)
            is FunctionCall -> this.handle(state)
            is ReturnExpression -> this.handle(state, currentFunction!!)
            is ExpressionSequence -> newState = VariableTypeState(state)
            is FunctionDeclaration -> {
                this.handle(state)
                return
            }
        }
        for (component in this.components) {
            component.detectTypes(newState, currentFunction)
        }
    }

    private fun SetVariable.handle(state: VariableTypeState) {
        val expectedType = state.getType(name)!!
        ensureCorrectType(name, value, expectedType, state)
    }

    private fun DefineVariable.handle(state: VariableTypeState) {
        var expected = this.expectedType?.mapToExpanded()
        if(expected != null) {
            ensureCorrectType(this.name, this.value, expected, state)
        } else {
            expected = value.calculateType(state) ?: throw ParseException("Unable to infer type for '$name'. Please specify it manually")
            this.setExpectedType(expected.type)
        }
        state.define(name, expected)
    }

    private fun FunctionCall.handle(state: VariableTypeState) {
        val funcType = function.calculateType(state).orElse {
            logWarning("Unable to infer type for function call")
            return
        }
        val functionName = if(function is GetVariable) "'${function.name}'" else "function"
        if(funcType !is FunctionType) {
            throw ParseException("Cannot invoke $functionName - actual type is $funcType")
        }
        val args = function.findFunctionParams(state).orElse {
            logWarning("Unable to validate function args for $functionName")
            return
        }
        if(arguments.size != args.size) throw ParseException("Wrong number of args for '$functionName'")
        for(i in args.indices) {
            val (name, expectedType) = args[i]
            ensureCorrectType(name, arguments[i], expectedType.mapToExpanded(), state)
        }
    }

    private fun FunctionDeclaration.handle(state: VariableTypeState) {
        val functionState = state.initialiseFunction(this) { it.mapToExpanded() }
        val newFunc = FunctionTypeData(this)
        for(component in components) {
            component.detectTypes(functionState, newFunc)
        }

        val returnTypes = newFunc.returnTypes
        val returnType = when(returnTypes.size) {
            0 -> {
                logWarning("No return types found for function $name")
                null
            }
            1 -> returnTypes.first()
            else -> {
                logWarning("Multiple return types found for function $name")
                newFunc.findCommonReturnType()
            }
        } ?: returnType.mapToExpanded()
        println("The return type for $name is $returnType")
        state.define(name, FunctionType(returnType))
    }

    private fun ReturnExpression.handle(state: VariableTypeState, currentFunction: FunctionTypeData) {
        if(toReturn == null) return
        //TODO Recursive functions
        val returnType = toReturn.calculateType(state).orElse {
            logWarning("Unable to detect return type path for ${currentFunction.function.name}")
            return
        }
        currentFunction.addReturnType(returnType)
    }

    //TODO Somewhere here need the proper expanded return type
    private fun Computable.calculateType(state: VariableTypeState): ExpandedType? = when (this) {
        is Data -> type.mapToExpanded()
        is FunctionDeclaration -> returnType.mapToExpanded()
        is GetVariable -> state.getType(name)
        is MathOperation -> IntType
        is FunctionCall -> {
            val type = function.calculateType(state)
            if(type is FunctionType) type.returnType else null
        }
        else -> null
    }

    private fun DataComputable.findFunctionParams(state: VariableTypeState): ParameterList? = when(this) {
        is FunctionDeclaration -> parameters
        is GetVariable -> state.getFunctionParams(name)
        else -> null
    }

    private fun DataType.mapToExpanded() = when(this) {
        DataType.INT -> IntType
        DataType.STRING -> StringType
        DataType.VOID -> VoidType
        DataType.ANYTHING -> AnythingType
        DataType.FUNCTION -> FunctionType(null) //Cannot get function type without more information
    }

    private fun ensureCorrectType(name: String, value: Computable, expected: ExpandedType, state: VariableTypeState) {
        val actualType = value.calculateType(state).orElse {
            logWarning("Unable to validate type for '$name'. Assuming is valid")
            return
        }
        if(!isValidType(expected, actualType)) {
            throw ParseException("Invalid type for '$name'. Expected $expected, got $actualType")
        }
    }

    private fun isValidType(expected: ExpandedType, actual: ExpandedType): Boolean {
        var expected: ExpandedType? = expected
        var actual: ExpandedType? = actual
        while(expected != null && actual != null) {
            if(expected == actual || expected == AnythingType) return true
            if(expected !is FunctionType || actual !is FunctionType) return false

            expected = expected.returnType
            actual = actual.returnType
        }
        return false
    }

    private fun logWarning(message: String) {
        println(message)
    }
}