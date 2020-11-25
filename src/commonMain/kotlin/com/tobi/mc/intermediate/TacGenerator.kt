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
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Negation
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.operation.UnaryMinus
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableContext
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.intermediate.construct.ControlLabel
import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

class TacGenerator private constructor() {

    private var labelCounter = 0
    private val controlLabels: MutableStack<ControlLabel> = ArrayListStack()

    //Using a LinkedHashMap so that the values iterating through the map is guaranteed to be in numeric order
    private val stringIndices: MutableMap<String, Int> = LinkedHashMap()

    companion object {

        fun toTac(program: Program): TacProgram {
            return TacGenerator().toTac(program)
        }
    }

    private fun toTac(program: Program): TacProgram {
        val globalFunction = FunctionDeclaration("global", emptyList(), ExpressionSequence(listOf(
            FunctionDeclaration("main", emptyList(), ExpressionSequence(ArrayList(program.code.operations).apply {
                add(ReturnStatement(null))
            }), DataType.VOID),
            FunctionCall(GetVariable("main", 0), emptyList()),
            FunctionCall(GetVariable("exit", -1), listOf(DataTypeInt(0)))
        )), DataType.VOID)

        val globalEnvironment = TacEnvironment(null)
        val treePosition = TreePositionData(-2, 0, globalFunction.body, LinkedHashMap(), LinkedHashSet(), LinkedHashMap())
        val inbuiltVariables = ArrayList(program.context.getVariables().filter {
            program.findVariable(it.key, 0, true) != null
        }.map {
            Pair(it.key, it.value.type)
        })
        inbuiltVariables.add("exit" to DataType.FUNCTION)
        for ((name, _) in inbuiltVariables) {
            globalEnvironment.addVariable(name)
            treePosition.environmentVariables[0 to name] = 0
        }

        val ops = ArrayList<TacStructure>()
        ops.addAll(inbuiltVariables.map { (name, _) ->
            ConstructSetVariable(EnvironmentVariable(name, 0), TacInbuiltFunction(name))
        })

        globalFunction.body.toTac(
            globalEnvironment,
            ops,
            treePosition
        )
        return TacProgram(stringIndices.keys.toTypedArray(), TacFunction("global", globalEnvironment, setOf("main"), ops, 0))
    }

    private fun Computable.toTac(
        currentEnvironment: TacEnvironment,
        code: MutableList<TacStructure>,
        positionData: TreePositionData
    ): Any = when(this) {
        is FunctionPrototype -> this.toTac(currentEnvironment, positionData)
        is FunctionDeclaration -> this.toTac(currentEnvironment, code, positionData)
        is FunctionCall -> this.toTac(currentEnvironment, RegisterUse(), code, null, positionData)
        is VariableDeclaration -> {
            val variable = if(isVariableInClosure(this.name, positionData.currentBlock)) {
                currentEnvironment.addVariable(this.name)
                positionData.createEnvironmentVariable(this.name)
            } else {
                positionData.createStackVariable(this.name)
            }
            code.add(ConstructSetVariable(variable, this.value.calculateIntermediate(currentEnvironment, RegisterUse(), code, positionData)))
        }
        is SetVariable -> {
            val variable = positionData.getVariable(this.contextIndex, this.name)
            code.add(ConstructSetVariable(
                variable,
                this.value.calculateIntermediate(currentEnvironment, RegisterUse(), code, positionData)
            ))
        }
        is IfStatement -> this.toTac(currentEnvironment, code, positionData)
        is WhileLoop -> this.toTac(currentEnvironment, code, positionData)
        is ContinueStatement -> code.add(ConstructGoto(controlLabels.peek().start))
        is BreakStatement -> code.add(ConstructGoto(controlLabels.peek().end))
        is ReturnStatement -> {
            if(toReturn != null) {
                code.add(ConstructSetVariable(ReturnedValue, this.toReturn!!.calculateIntermediate(currentEnvironment, RegisterUse(), code, positionData)))
            }
            code.add(ConstructReturn)
        }
        is ExpressionSequence -> this.getComponents().forEach {
            val newData = TreePositionData(
                contextDepth = positionData.contextDepth + 1,
                functionCount = positionData.functionCount,
                currentBlock = this,
                environmentVariables = positionData.environmentVariables,
                stackVariables = positionData.stackVariables,
                variableNameMapping = positionData.variableNameMapping
            )
            it.toTac(currentEnvironment, code, newData)
        }
        else -> throw IllegalStateException("Unknown computable ${this::class.simpleName}")
    }

    private fun FunctionPrototype.toTac(
        currentEnvironment: TacEnvironment,
        positionData: TreePositionData
    ) {
        if(isVariableInClosure(this.name, positionData.currentBlock)) {
            currentEnvironment.addVariable(name)
            positionData.createEnvironmentVariable(this.name)
        } else {
            positionData.createStackVariable(this.name)
        }
    }

    private fun FunctionDeclaration.toTac(
        currentEnvironment: TacEnvironment,
        code: MutableList<TacStructure>,
        positionData: TreePositionData
    ): TacFunction {
        val variable = positionData.getVariable(0, this.name)

        val newEnvironment = TacEnvironment(currentEnvironment)
        val newVars = HashMap(positionData.environmentVariables)

        val newData = TreePositionData(positionData.contextDepth + 1, positionData.functionCount + 1, this.body, newVars, LinkedHashSet(), LinkedHashMap())
        val newCode = ArrayList<TacStructure>()

        for((i, parameter) in this.parameters.withIndex()) {
            val variable = if(this.body.findVariable(parameter.name, 0, false) != null) {
                newEnvironment.addVariable(parameter.name)
                newData.createEnvironmentVariable(parameter.name)
            } else {
                newData.createStackVariable(parameter.name)
            }
            newCode.add(ConstructSetVariable(variable, ParamReference(i)))
        }

        this.body.toTac(newEnvironment, newCode, newData)
        val function = TacFunction(this.name, newEnvironment, newData.stackVariables, newCode, this.parameters.size)

        code.add(ConstructSetVariable(variable, function))

        return function
    }

    private fun Computable.calculateIntermediate(
        currentEnvironment: TacEnvironment,
        registers: RegisterUse,
        code: MutableList<TacStructure>,
        positionData: TreePositionData
    ): TacVariableReference {
        when(this) {
            is DataTypeInt -> return IntValue(this.value)
            is DataTypeString -> return StringVariable(findStringIndex(this.value))
            is GetVariable -> return positionData.getVariable(this.contextIndex, this.name)
        }
        registers.beginOperation()
        val newRegister = RegisterVariable(registers.findAvailable())

        if(this is FunctionCall) {
            this.toTac(currentEnvironment, registers, code, newRegister, positionData)
            return newRegister
        }

        val expression = when(this) {
            is MathOperation -> this.toTac(currentEnvironment, registers, code, positionData)
            is UnaryMinus -> this.toTac(currentEnvironment, registers, code, positionData)
            is Negation -> this.toTac(currentEnvironment, registers, code, positionData)
            is StringConcat -> this.toTac(currentEnvironment, registers, code, positionData)
            else -> throw IllegalArgumentException("Unknown computable ${this::class.simpleName}")
        }
        code.add(ConstructSetVariable(newRegister, expression))
        return newRegister
    }

    private fun FunctionCall.toTac(
        currentEnvironment: TacEnvironment,
        registers: RegisterUse,
        code: MutableList<TacStructure>,
        assignment: TacMutableVariable?,
        positionData: TreePositionData
    ) {
        for(arg in this.arguments) {
            val actualArg = arg.calculateIntermediate(currentEnvironment, registers, code, positionData)
            code.add(ConstructPushArgument(actualArg))
        }
        code.add(ConstructFunctionCall(this.function.calculateIntermediate(currentEnvironment, registers, code, positionData)))
        if(assignment != null) {
            code.add(ConstructSetVariable(assignment, ReturnedValue))
        }
        for(arg in this.arguments) {
            code.add(ConstructPopArgument)
        }
    }

    private fun MathOperation.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacStructure>, positionData: TreePositionData): ConstructMath {
        val first = this.arg1.calculateIntermediate(currentEnvironment, registers, code, positionData)
        val second = this.arg2.calculateIntermediate(currentEnvironment, registers, code, positionData)

        return ConstructMath(first, ConstructMath.getType(this), second)
    }

    private fun UnaryMinus.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacStructure>, positionData: TreePositionData): ConstructUnaryMinus {
        val exp = this.expression.calculateIntermediate(currentEnvironment, registers, code, positionData)
        return ConstructUnaryMinus(exp)
    }

    private fun Negation.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacStructure>, positionData: TreePositionData): ConstructNegation {
        val toNegate = this.negation.calculateIntermediate(currentEnvironment, registers, code, positionData)
        return ConstructNegation(toNegate)
    }

    private fun StringConcat.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacStructure>, positionData: TreePositionData): ConstructStringConcat {
        val str1 = this.str1.calculateIntermediate(currentEnvironment, registers, code, positionData)
        val str2 = this.str2.calculateIntermediate(currentEnvironment, registers, code, positionData)

        return ConstructStringConcat(str1, str2)
    }

    private fun IfStatement.toTac(
        environment: TacEnvironment,
        code: MutableList<TacStructure>,
        positionData: TreePositionData
    ) {
        val endLabel = generateLabel()
        val elseLabel = if(elseBody == null) endLabel else generateLabel()

        code.add(ConstructBranchEqualZero(check.calculateIntermediate(environment, RegisterUse(), code, positionData), elseLabel))
        this.ifBody.toTac(environment, code, positionData)
        code.add(ConstructGoto(endLabel))

        if(elseBody != null) {
            code.add(ConstructLabel(elseLabel))
            elseBody!!.toTac(environment, code, positionData)
            code.add(ConstructGoto(endLabel))
        }
        code.add(ConstructLabel(endLabel))
    }

    private fun WhileLoop.toTac(
        environment: TacEnvironment,
        code: MutableList<TacStructure>,
        positionData: TreePositionData
    ) {
        val startLabel = generateLabel()
        val endLabel = generateLabel()
        code.add(ConstructLabel(startLabel))

        code.add(ConstructBranchEqualZero(check.calculateIntermediate(environment, RegisterUse(), code, positionData), endLabel))
        controlLabels.push(
            ControlLabel(startLabel, endLabel)
        )
        body.toTac(environment, code, positionData)
        controlLabels.pop()
        code.add(ConstructGoto(startLabel))
        code.add(ConstructLabel(endLabel))
    }

    private fun isVariableInClosure(name: String, currentScope: ExpressionSequence): Boolean {
        val code = currentScope.operations
        for(i in 0 until code.size) {
            if(code[i].findVariable(name, 0, false) != null) {
                return true
            }
        }
        return false
    }

    private fun Computable.findVariable(name: String, currentDepth: Int, isInFunction: Boolean): Computable? = when(this) {
        is VariableContext ->
            if(this.contextIndex == currentDepth && isInFunction && this.name == name) this else null
        is ExpressionSequence -> this.operations.asSequence().map {
            it.findVariable(name, currentDepth + 1, isInFunction)
        }.filterNotNull().firstOrNull()
        is FunctionDeclaration -> this.body.findVariable(name, currentDepth + 1, true)
        else -> getComponents().asSequence().map {
            it.findVariable(name, currentDepth, isInFunction)
        }.filterNotNull().firstOrNull()
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

    private fun generateLabel(): String {
        val label = "label$labelCounter"
        labelCounter++
        return label
    }
}