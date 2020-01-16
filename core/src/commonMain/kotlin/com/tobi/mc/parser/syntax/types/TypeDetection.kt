package com.tobi.mc.parser.syntax.types

import com.tobi.mc.Data
import com.tobi.mc.DefaultContext
import com.tobi.mc.analysis.*
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.parser.ParseException
import com.tobi.util.orElse

object TypeDetection {

    fun checkAndDetectTypes(program: Program, defaultContext: DefaultContext) {
        val parentState = VariableTypeState(null)

        for(variable in defaultContext.defaultVariables) {
            parentState.define(variable.name, variable.expandedType.mapToType())
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
        //TODO Intersection types come into play here
        val expectedType = state.getType(name)!!
        ensureCorrectType(name, value, expectedType, state)
    }

    private fun DefineVariable.handle(state: VariableTypeState) {
        var expected = this.expectedType?.mapToType()
        if(expected != null) {
            ensureCorrectType(this.name, this.value, expected, state)
        } else {
            expected = value.calculateType(state)
            if(expected == null || expected !is CompleteType) {
                throw ParseException("Unable to infer type for '$name'. Please specify it manually")
            }
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
        if(funcType !is AnalysisFunctionType) {
            throw ParseException("Cannot invoke $functionName - actual type is $funcType")
        }
        val args = function.findFunctionParams(state)
        if(args == null || args !is AnalysisKnownParams) {
            logWarning("Unable to validate function args for $functionName")
            return
        }

        if(arguments.size != args.size) throw ParseException("Wrong number of args for '$functionName'")
        for(i in args.params.indices) {
            val expectedType = args.params[i]
            //TODO Do we want to show the name of functions args in the error?
            ensureCorrectType("Function argument", arguments[i], expectedType, state)
        }
    }

    private fun FunctionDeclaration.handle(state: VariableTypeState) {
        val (functionState, functionType) = state.initialiseFunction(this) { it.mapToType() }
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
        } ?: returnType.mapToType()
        println("The return type for $name is $returnType")
        state.define(name, AnalysisFunctionType(returnType, functionType.parameters))
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
    private fun Computable.calculateType(state: VariableTypeState): AnalysisType? = when (this) {
        is Data -> type.mapToType()
        is FunctionDeclaration -> returnType.mapToType()
        is GetVariable -> state.getType(name)
        is MathOperation -> AnalysisIntType
        is FunctionCall -> {
            val type = function.calculateType(state)
            if(type is AnalysisFunctionType) type.returnType else null
        }
        else -> null
    }

    private fun DataComputable.findFunctionParams(state: VariableTypeState): AnalysisParamList? = when(this) {
        is FunctionDeclaration -> AnalysisKnownParams(this.parameters.map { (_, type) -> type.mapToType() })
        is GetVariable -> {
            val type = state.getType(this.name)
            if(type is AnalysisFunctionType) type.parameters else null
        }
        else -> null
    }

    private fun DataType.mapToType(): AnalysisType = when(this) {
        DataType.INT -> AnalysisIntType
        DataType.STRING -> AnalysisStringType
        DataType.VOID -> AnalysisVoidType
        DataType.ANYTHING -> AnalysisAnythingType
        DataType.FUNCTION -> AnalysisFunctionType(AnalysisUnknownType, AnalysisUnknownParams) //Cannot get function type without more information
    }

    private fun ExpandedType.mapToType(): AnalysisType = when(this) {
        is IntType -> AnalysisIntType
        is StringType -> AnalysisStringType
        is VoidType -> AnalysisVoidType
        is AnythingType -> AnalysisAnythingType
        is FunctionType -> AnalysisFunctionType(
            this.returnType.mapToType(),
            AnalysisKnownParams(this.params.map { (_, type) -> type.mapToType() })
        )
    }

    private fun ensureCorrectType(name: String, value: Computable, expected: AnalysisType, state: VariableTypeState) {
        val actualType = value.calculateType(state).orElse {
            logWarning("Unable to validate type for '$name'. Assuming is valid")
            return
        }
        if(!isValidType(expected, actualType)) {
            throw ParseException("Invalid type for '$name'. Expected $expected, got $actualType")
        }
    }

    private fun isValidType(expected: AnalysisType, actual: AnalysisType): Boolean {
        return true
//        var expected: AnalysisType? = expected
//        var actual: AnalysisType? = actual
//        while(expected != null && actual != null) {
//            if(expected == actual || expected == AnythingType) return true
//            if(expected !is FunctionType || actual !is FunctionType) return false
//
//            expected = expected.returnType
//            actual = actual.returnType
//        }
//        return false
    }

    private fun logWarning(message: String) {
        println(message)
    }
}