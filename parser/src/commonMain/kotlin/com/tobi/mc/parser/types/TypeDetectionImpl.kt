package com.tobi.mc.parser.types

import com.tobi.mc.ParseException
import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.parser.TypeDetection
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.type.*
import com.tobi.util.DescriptionMeta

internal object TypeDetectionImpl : TypeDetection {

    override val description: DescriptionMeta = SimpleDescription("Type Detection & Validation", """
        Infers types when the 'auto' keyword is used, and ensures types are and operations are possible
    """.trimIndent())

    override fun processProgram(program: Program): Program = program.apply {
        val defaultContext = DefaultContext()
        val state = createNewState(defaultContext)

        forEach {
            it.detectTypes(state, null)
        }
    }

    override fun <T : Computable> inferAndValidateTypes(computable: T, defaultContext: DefaultContext) {
        computable.detectTypes(createNewState(defaultContext), null)
    }

    private fun createNewState(defaultContext: DefaultContext): VariableTypeState {
        val parentState = VariableTypeState(null)
        for(variable in defaultContext.defaultVariables) {
            parentState.define(variable.name, variable.expandedType)
        }
        return VariableTypeState(parentState)
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
        for (component in this.getComponents()) {
            component.detectTypes(newState, currentFunction)
        }
    }

    private fun SetVariable.handle(state: VariableTypeState) {
        val expectedType = state.getType(name)!!
        val actualType = value.calculateType(state)
        setTypes(name, state, expectedType, actualType)
    }

    private fun DefineVariable.handle(state: VariableTypeState) {
        var expected = this.expectedType?.mapToType()
        val actualType = this.value.calculateType(state)

        if(expected == null) {
            val simpleType = TypeMerger.getSimpleType(actualType) ?:
                throw ParseException("Unable to infer type for '$name'. Please specify it manually")
            this.expectedType = simpleType
            expected = actualType
        }
        if(expectedType == DataType.VOID || actualType is VoidType) {
            throw ParseException("Can't define variable '$name' as void")
        }

        state.define(name, expected)
        setTypes(
            name,
            state,
            expected,
            actualType,
            expectedType!!
        )
    }

    private fun setTypes(name: String, state: VariableTypeState, currentType: ExpandedType, newType: ExpandedType, restriction: DataType? = null) {
        if(!TypeMerger.canBeAssignedTo(currentType, newType)) {
            throw ParseException("$name: $currentType cannot be assigned to '$newType'")
        }
        var mergedType = TypeMerger.mergeTypes(currentType, newType)
        if(restriction != null) {
            mergedType =
                TypeMerger.restrictType(mergedType, restriction)
        }
        state.set(name, mergedType)
    }

    private fun FunctionCall.handle(state: VariableTypeState) {
        val funcType = function.calculateType(state)
        val args = this.arguments.map { it.calculateType(state) }
        TypeMerger.invokeFunction(funcType, args)
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
            throw ParseException("Unable to infer return type for function '${this.name}. Please specify it manually'")
        }
        if(this.returnType == null) {
            this.returnType = simpleReturnType
        }

        state.define(name,
            FunctionType(returnType, functionType.parameters)
        )
    }

    private fun ReturnExpression.handle(state: VariableTypeState, currentFunction: FunctionTypeData) {
        if(toReturn == null) {
            currentFunction.addReturnType(VoidType)
        } else {
            val returnType = toReturn!!.calculateType(state)
            currentFunction.addReturnType(returnType)
        }
    }

    private fun Computable.calculateType(state: VariableTypeState): ExpandedType = when (this) {
        is Data -> type.mapToType()
        is FunctionDeclaration -> {
            val returnType = this.returnType ?: throw IllegalStateException()
            returnType.mapToType()
        }
        is GetVariable -> state.getType(name)!!
        is MathOperation -> IntType
        is FunctionCall -> {
            val function = function.calculateType(state)
            val args = this.arguments.map { it.calculateType(state) }
            TypeMerger.invokeFunction(function, args)
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