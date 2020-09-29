package com.tobi.mc.intermediate

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.intermediate.construct.ControlLabel
import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.parser.util.getComponents

class TacGenerator(private val program: Program) {

    private val functions: MutableList<TacFunction> = ArrayList()
    private val environments: MutableList<TacEnvironment> = ArrayList()

    //Using a LinkedHashMap so that the values iterating through the map is guaranteed to be in numeric order
    private val stringIndices: MutableMap<String, Int> = LinkedHashMap()

    fun toTac(): TacProgram {
        functions.clear()
        environments.clear()

        FunctionDeclaration("main", emptyList(), program.code, DataType.VOID).toTac(TacEnvironment("global", null), TacGenerationContext(), ArrayList())

        return TacProgram(stringIndices.keys.toTypedArray(), environments, functions)
    }

    private fun Computable.toTac(currentEnvironment: TacEnvironment, context: TacGenerationContext, code: MutableList<TacCodeConstruct>): Any = when(this) {
        is FunctionDeclaration -> {
            code.add(ConstructCreateClosure(currentEnvironment))

            val newEnvironment = this.createEnvironment(currentEnvironment)
            environments.add(newEnvironment)

            val newCode = ArrayList<TacCodeConstruct>()
            for((i, parameter) in this.parameters.withIndex()) {
                newCode.add(ConstructSetVariable(EnvironmentVariable(parameter.name), ParamReference(i)))
            }

            this.body.toTac(newEnvironment, TacGenerationContext(), newCode)
            functions.add(TacFunction(this.name, newEnvironment, newCode))
        }
        is FunctionCall -> this.toTac(RegisterUse(), code, null)
        is DefineVariable -> code.add(ConstructSetVariable(
            EnvironmentVariable(this.name),
            this.value.calculateIntermediate(RegisterUse(), code)
        ))
        is SetVariable -> code.add(ConstructSetVariable(
            EnvironmentVariable(this.name),
            this.value.calculateIntermediate(RegisterUse(), code)
        ))
        is IfStatement -> this.toTac(currentEnvironment, context, code)
        is WhileLoop -> this.toTac(currentEnvironment, context, code)
        is ContinueStatement -> code.add(ConstructGoto(context.controlLabels.peek().start))
        is BreakStatement -> code.add(ConstructGoto(context.controlLabels.peek().end))
        is ReturnStatement -> code.add(ConstructReturn(this.toReturn?.calculateIntermediate(RegisterUse(), code)))
        is ExpressionSequence -> this.operations.forEach {
            it.toTac(currentEnvironment, context, code)
        }
        else -> throw IllegalStateException()
    }

    private fun Computable.calculateIntermediate(registers: RegisterUse, code: MutableList<TacCodeConstruct>): TacVariableReference {
        when(this) {
            is DataTypeInt -> return IntValue(this.value)
            is GetVariable -> return EnvironmentVariable(this.name)
            is DataTypeString -> return StringVariable(findStringIndex(this.value))
        }
        registers.beginOperation()
        val newRegister = RegisterVariable(registers.findAvailable())

        if(this is FunctionCall) {
            this.toTac(registers, code, newRegister)
            return newRegister
        }

        val expression = when(this) {
            is MathOperation -> this.toTac(registers, code)
            is UnaryMinus -> this.toTac(registers, code)
            is Negation -> this.toTac(registers, code)
            is StringConcat -> this.toTac(registers, code)
            else -> throw IllegalArgumentException("Unknown computable ${this::class.simpleName}")
        }
        code.add(ConstructSetVariable(newRegister, expression))
        return newRegister
    }

    private fun FunctionCall.toTac(registers: RegisterUse, code: MutableList<TacCodeConstruct>, assignment: TacMutableVariable?) {
        for(arg in this.arguments) {
            val actualArg = arg.calculateIntermediate(registers, code)
            code.add(ConstructPushArgument(actualArg))
        }
        val functionCall = ConstructFunctionCall(this.function.calculateIntermediate(registers, code))
        code.add(if(assignment != null) {
            ConstructSetVariable(assignment, functionCall)
        } else {
            functionCall
        })
        for(arg in this.arguments) {
            code.add(ConstructPopArgument)
        }
    }

    private fun MathOperation.toTac(registers: RegisterUse, code: MutableList<TacCodeConstruct>): ConstructMath {
        val first = this.arg1.calculateIntermediate(registers, code)
        val second = this.arg2.calculateIntermediate(registers, code)

        return ConstructMath(first, ConstructMath.getType(this), second)
    }

    private fun UnaryMinus.toTac(registers: RegisterUse, code: MutableList<TacCodeConstruct>): ConstructUnaryMinus {
        val exp = this.expression.calculateIntermediate(registers, code)
        return ConstructUnaryMinus(exp)
    }

    private fun Negation.toTac(registers: RegisterUse, code: MutableList<TacCodeConstruct>): ConstructNegation {
        val toNegate = this.negation.calculateIntermediate(registers, code)
        return ConstructNegation(toNegate)
    }

    private fun StringConcat.toTac(registers: RegisterUse, code: MutableList<TacCodeConstruct>): ConstructStringConcat {
        val str1 = this.str1.calculateIntermediate(registers, code)
        val str2 = this.str2.calculateIntermediate(registers, code)

        return ConstructStringConcat(str1, str2)
    }

    private fun IfStatement.toTac(environment: TacEnvironment, context: TacGenerationContext, code: MutableList<TacCodeConstruct>) {
        val endLabel = context.generateLabel()
        val elseLabel = if(elseBody == null) endLabel else context.generateLabel()

        code.add(ConstructBranchZero(check.calculateIntermediate(RegisterUse(), code), endLabel))
        this.ifBody.toTac(environment, context, code)
        code.add(ConstructGoto(endLabel))

        if(elseBody != null) {
            code.add(ConstructLabel(elseLabel))
            elseBody!!.toTac(environment, context, code)
            code.add(ConstructGoto(endLabel))
        }
        code.add(ConstructLabel(endLabel))
    }

    private fun WhileLoop.toTac(environment: TacEnvironment, context: TacGenerationContext, code: MutableList<TacCodeConstruct>) {
        val startLabel = context.generateLabel()
        val endLabel = context.generateLabel()
        code.add(ConstructLabel(startLabel))

        code.add(ConstructBranchZero(check.calculateIntermediate(RegisterUse(), code), endLabel))
        context.controlLabels.push(
            ControlLabel(startLabel, endLabel)
        )
        body.toTac(environment, context, code)
        context.controlLabels.pop()
        code.add(ConstructGoto(startLabel))
        code.add(ConstructLabel(endLabel))
    }

    private fun FunctionDeclaration.createEnvironment(parent: TacEnvironment?): TacEnvironment = TacEnvironment(name, parent).apply {
        for((type, name) in parameters) {
            addVariable(name, type)
        }
        for (operation in body.operations) {
            operation.findVariables(this)
        }
    }

    private fun Computable.findVariables(environment: TacEnvironment): Unit = when(this) {
        is FunctionDeclaration -> environment.addVariable(this.name, DataType.FUNCTION)
        is DefineVariable -> {
            val type = this.expectedType ?: throw IllegalStateException()
            environment.addVariable(this.name, type)
        }
        else -> this.getComponents().forEach { it.findVariables(environment) }
    }

    private fun findStringIndex(text: String): Int {
        val existingIndex = stringIndices[text]
        if(existingIndex != null) {
            return existingIndex
        }

        val newIndex = stringIndices.size
        stringIndices[text] = newIndex
        return newIndex
    }
}