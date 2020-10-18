package com.tobi.mc.parser.types

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.type.*

/**
 * Performs two roles:
 * 1. Infers types when the 'auto' keyword is used
 * 2. Ensures types and operations are possible
 */
internal object TypeDetector {

    fun inferAndValidateTypes(program: Program) {
        program.detectTypes(
            createNewState(program.context),
            FunctionTypeData(FunctionDeclaration("main", emptyList(), program.code, DataType.VOID), VoidType)
        )
    }

    fun inferAndValidateTypes(computable: Computable, context: Context) {
        computable.detectTypes(createNewState(context), null)
    }

    private fun createNewState(context: Context): VariableTypeState {
        val parentState = VariableTypeState(null)
        for((name, value) in context.getVariables()) {
            if(value !is TypedComputable) {
                throw IllegalArgumentException("No type information for variable $name")
            }
            parentState.define(name, value.expandedType)
        }
        return VariableTypeState(parentState)
    }

    private fun Computable.detectTypes(state: VariableTypeState, currentFunction: FunctionTypeData?) {
        var newState = state
        when(this) {
            is FunctionCall -> this.handle(state)
            is ReturnStatement -> this.handle(state, currentFunction!!)
            is ExpressionSequence -> newState = VariableTypeState(state)
            is MathOperation -> {
                val name = this::class.simpleName!!.toLowerCase()
                arg1.ensureNumeric(name, state)
                arg2.ensureNumeric(name, state)
            }
            is UnaryMinus -> expression.ensureNumeric("unary minus", state)
            is Negation -> negation.ensureNumeric("negation", state)
            is StringConcat -> {
                str1.ensureString("string concatenation", state)
                str2.ensureString("string concatenation", state)
            }
            is IfStatement -> check.ensureNumeric("if", state)
            is WhileLoop -> check.ensureNumeric("while", state)
            is FunctionDeclaration -> {
                this.handle(state)
                return
            }
        }
        for (component in this.getComponents()) {
            component.detectTypes(newState, currentFunction)
        }

        when(this) {
            is SetVariable -> this.handle(state)
            is DefineVariable -> this.handle(state)
        }
    }

    private fun SetVariable.handle(state: VariableTypeState) {
        val currentType = state.getType(name)!!
        val assignedType = value.calculateType(state)

        if(!TypeMerger.canBeAssignedTo(currentType, assignedType)) {
            throw ParseException("$assignedType cannot be assigned to $currentType", this)
        }
        if(currentType is ComplexType) {
            var mergedType = TypeMerger.mergeTypes(currentType, assignedType)
            mergedType = TypeMerger.restrictType(this, mergedType, TypeMerger.getSimpleType(currentType)!!)
            state.set(name, mergedType)
        }
    }

    private fun DefineVariable.handle(state: VariableTypeState) {
        val expected = this.expectedType?.mapToType()
        val actualType = this.value.calculateType(state)

        if(expected == null) {
            val simpleType = TypeMerger.getSimpleType(actualType) ?:
                throw ParseException("Unable to infer type. Please specify it manually", this)
            this.expectedType = simpleType
        } else {
            if(!TypeMerger.canBeAssignedTo(expected, actualType)) {
                throw ParseException("$actualType cannot be assigned to $expected", this)
            }
        }
        if(expectedType == DataType.VOID || actualType is VoidType) {
            throw ParseException("Can't define a variable as void", this)
        }
        state.define(name, actualType)
    }

    private fun FunctionCall.handle(state: VariableTypeState) {
        val funcType = function.calculateType(state)
        val args = this.arguments.map { it.calculateType(state) }
        TypeMerger.invokeFunction(this, funcType, args)
    }

    private fun FunctionDeclaration.handle(state: VariableTypeState) {
        val (functionState, functionType) = state.initialiseFunction(this) { it.mapToType() }
        val newFunc = FunctionTypeData(this, this.returnType?.mapToType())

        for(component in getComponents()) {
            component.detectTypes(functionState, newFunc)
        }

        val returnType = newFunc.getReturnType()
        val simpleReturnType = returnType.run(TypeMerger::getSimpleType)

        if(simpleReturnType == null) {
            throw ParseException("Unable to infer return type. Please specify it manually'", this)
        }
        if(this.returnType == null) {
            this.returnType = simpleReturnType
        }

        state.define(name,
            FunctionType(returnType, functionType.parameters)
        )
    }

    private fun ReturnStatement.handle(state: VariableTypeState, currentFunction: FunctionTypeData) {
        if(toReturn == null) {
            currentFunction.addReturnType(this, VoidType)
        } else {
            val returnType = toReturn!!.calculateType(state)
            currentFunction.addReturnType(this, returnType)
        }
    }

    private fun Computable.ensureNumeric(name: String, state: VariableTypeState) {
        ensureType(name, IntType, "int", state)
    }

    private fun Computable.ensureString(name: String, state: VariableTypeState) {
        ensureType(name, StringType, "string", state)
    }

    private fun Computable.ensureType(name: String, type: ExpandedType, typeDescription: String, state: VariableTypeState) {
        val thisType = this.calculateType(state)
        if(!TypeMerger.canBeAssignedTo(type, thisType)) {
            throw ParseException("Expected an expression of type $typeDescription, got $type", this)
        }
    }

    private fun Computable.calculateType(state: VariableTypeState): ExpandedType = when (this) {
        is TypedComputable -> this.expandedType
        is Data -> type.mapToType()
        is FunctionDeclaration -> {
            val returnType = this.returnType ?: throw IllegalStateException()
            returnType.mapToType()
        }
        is GetVariable -> state.getType(name)!!
        is MathOperation, is Negation, is UnaryMinus -> IntType
        is StringConcat -> StringType
        is FunctionCall -> {
            val function = function.calculateType(state)
            val args = this.arguments.map { it.calculateType(state) }
            TypeMerger.invokeFunction(this, function, args)
        }
        else -> throw IllegalStateException()
    }

    private fun DataType.mapToType(): ExpandedType = when(this) {
        DataType.INT -> IntType
        DataType.STRING -> StringType
        DataType.VOID -> VoidType
        //Cannot get function type without more information
        DataType.FUNCTION -> FunctionType(
            UnknownType,
            UnknownParameters
        )
    }
}