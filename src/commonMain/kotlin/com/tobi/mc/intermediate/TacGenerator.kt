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
import com.tobi.mc.intermediate.code.*
import com.tobi.mc.intermediate.optimisation.Optimisations
import com.tobi.mc.intermediate.optimisation.TacOptimisation
import com.tobi.mc.intermediate.optimisation.TacOptimiser
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

class TacGenerator(val optimisations: List<TacOptimisation> = Optimisations.ALL_OPTIMISATIONS) {

    private val whileLoopControlBlocks: MutableStack<WhileLoopControlBlock> = ArrayListStack()

    //Using a LinkedHashMap so that the values iterating through the map is guaranteed to be in numeric order
    private val stringIndices: MutableMap<String, Int> = LinkedHashMap()

    fun toTac(program: Program): TacProgram {
        val tac = visitProgram(program)
        return TacOptimiser(this.optimisations).optimise(tac) as TacProgram
    }

    private fun visitProgram(program: Program): TacProgram {
        val globalFunction = FunctionDeclaration("global", emptyList(), ExpressionSequence(
            FunctionDeclaration("main", emptyList(), ExpressionSequence(ArrayList(program.code.expressions).apply {
                add(ReturnStatement(null))
            }), DataType.VOID),
            FunctionCall(GetVariable("main", 0), emptyList()),
            FunctionCall(GetVariable("exit", -1), listOf(DataTypeInt(0)))
        ), DataType.VOID)

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

        val ops = ArrayList<TacNode>()
        ops.addAll(inbuiltVariables.map { (name, _) ->
            TacSetVariable(EnvironmentVariable(name, 0), TacInbuiltFunction(name))
        })

        val body = visitSequence(globalFunction.body, globalEnvironment, treePosition, null)
        body.instructions.addAll(0, ops)

        return TacProgram(stringIndices.keys.toTypedArray(), TacFunction(globalEnvironment, setOf("main"), body, 0))
    }

    private fun visitNode(
        computable: Computable,
        currentEnvironment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData,
        currentSequence: Iterator<Computable>,
        currentBlock: TacBlock?
    ): Any = when(computable) {
        is FunctionPrototype -> visitFunctionPrototype(computable, currentEnvironment, positionData)
        is FunctionDeclaration -> visitFunctionDeclaration(computable, currentEnvironment, code, positionData)
        is FunctionCall -> visitFunctionCall(computable, currentEnvironment, RegisterUse(), code, null, positionData)
        is VariableDeclaration -> visitVariableDeclaration(computable, currentEnvironment, code, positionData)
        is SetVariable -> visitSetVariable(computable, currentEnvironment, code, positionData)
        is IfStatement -> visitIfStatement(computable, currentEnvironment, code, positionData, currentSequence, currentBlock)
        is WhileLoop -> visitWhileLoop(computable, currentEnvironment, code, positionData, currentSequence, currentBlock)
        is ContinueStatement -> code.add(TacGoto(whileLoopControlBlocks.peek().start))
        is BreakStatement -> code.add(TacGoto(whileLoopControlBlocks.peek().end))
        is ReturnStatement -> visitReturnStatement(computable, currentEnvironment, code, positionData)
        is ExpressionSequence -> visitSequence(computable, currentEnvironment, positionData, currentBlock)
        else -> throw IllegalStateException("Unknown node ${this::class.simpleName}")
    }

    private fun visitFunctionPrototype(
        prototype: FunctionPrototype,
        environment: TacEnvironment,
        positionData: TreePositionData
    ) {
        if(isVariableInClosure(prototype.name, positionData.currentBlock)) {
            environment.addVariable(prototype.name)
            positionData.createEnvironmentVariable(prototype.name)
        } else {
            positionData.createStackVariable(prototype.name)
        }
    }

    private fun visitFunctionDeclaration(
        function: FunctionDeclaration,
        currentEnvironment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData
    ): TacFunction {
        val functionVariable = positionData.getVariable(0, function.name)
        val newEnvironment = TacEnvironment(currentEnvironment)
        val newData = positionData.enterFunction(function)

        val paramSetInstructions = ArrayList<TacNode>()
        for((i, parameter) in function.parameters.withIndex()) {
            val paramVariable = if(function.body.findVariable(parameter.name, 0, false) != null) {
                newEnvironment.addVariable(parameter.name)
                newData.createEnvironmentVariable(parameter.name)
            } else {
                newData.createStackVariable(parameter.name)
            }
            paramSetInstructions.add(TacSetVariable(paramVariable, ParamReference(i)))
        }

        val returnBlock = TacBlock(arrayListOf(TacReturn))
        val functionBodyBlock = visitSequence(function.body, newEnvironment, newData, returnBlock)
        functionBodyBlock.instructions.addAll(0, paramSetInstructions)

        val tacFunction = TacFunction(newEnvironment, newData.stackVariables, functionBodyBlock, function.parameters.size)

        code.add(TacSetVariable(functionVariable, tacFunction))

        return tacFunction
    }

    private fun visitVariableDeclaration(
        variableDeclaration: VariableDeclaration,
        environment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData
    ) {
        val name = variableDeclaration.name
        val assignment = variableDeclaration.value.calculateIntermediate(environment, RegisterUse(), code, positionData)

        val variable = if(isVariableInClosure(name, positionData.currentBlock)) {
            environment.addVariable(name)
            positionData.createEnvironmentVariable(name)
        } else {
            positionData.createStackVariable(name)
        }
        code.add(TacSetVariable(variable, assignment))
    }

    private fun visitSetVariable(
        setVariable: SetVariable,
        environment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData
    ) {
        val variable = positionData.getVariable(setVariable.contextIndex, setVariable.name)
        code.add(TacSetVariable(
            variable,
            setVariable.value.calculateIntermediate(environment, RegisterUse(), code, positionData)
        ))
    }

    private fun Computable.calculateIntermediate(
        currentEnvironment: TacEnvironment,
        registers: RegisterUse,
        code: MutableList<TacNode>,
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
            visitFunctionCall(this, currentEnvironment, registers, code, newRegister, positionData)
            return newRegister
        }

        val expression = when(this) {
            is MathOperation -> this.toTac(currentEnvironment, registers, code, positionData)
            is UnaryMinus -> this.toTac(currentEnvironment, registers, code, positionData)
            is Negation -> this.toTac(currentEnvironment, registers, code, positionData)
            is StringConcat -> this.toTac(currentEnvironment, registers, code, positionData)
            else -> throw IllegalArgumentException("Unknown computable ${this::class.simpleName}")
        }
        code.add(TacSetVariable(newRegister, expression))
        return newRegister
    }

    private fun visitFunctionCall(
        functionCall: FunctionCall,
        environment: TacEnvironment,
        registers: RegisterUse,
        code: MutableList<TacNode>,
        assignment: TacMutableVariable?,
        positionData: TreePositionData
    ) {
        val function = functionCall.function.calculateIntermediate(environment, registers, code, positionData)
        for((i, arg) in functionCall.arguments.withIndex()) {
            val actualArg = arg.calculateIntermediate(environment, registers, code, positionData)
            code.add(TacSetArgument(actualArg, i))
        }
        code.add(TacFunctionCall(function))
        if(assignment != null) {
            code.add(TacSetVariable(assignment, ReturnedValue))
        }
    }

    private fun visitReturnStatement(
        returnStatement: ReturnStatement,
        environment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData
    ) {
        val toReturn = returnStatement.toReturn
        if(toReturn != null) {
            // If returning the result of a function call, the return register will already contain the resulting value,
            // otherwise an assignment is required to populate the return register with the returned value
            if(toReturn is FunctionCall) {
                visitFunctionCall(toReturn, environment, RegisterUse(), code, null, positionData)
            } else {
                code.add(TacSetVariable(ReturnedValue, toReturn.calculateIntermediate(environment, RegisterUse(), code, positionData)))
            }
        }
        code.add(TacReturn)
    }

    private fun MathOperation.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacNode>, positionData: TreePositionData): TacMathOperation {
        val first = this.arg1.calculateIntermediate(currentEnvironment, registers, code, positionData)
        val second = this.arg2.calculateIntermediate(currentEnvironment, registers, code, positionData)

        return TacMathOperation(first, TacMathOperation.getType(this), second)
    }

    private fun UnaryMinus.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacNode>, positionData: TreePositionData): TacUnaryMinus {
        val exp = this.expression.calculateIntermediate(currentEnvironment, registers, code, positionData)
        return TacUnaryMinus(exp)
    }

    private fun Negation.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacNode>, positionData: TreePositionData): TacNegation {
        val toNegate = this.negation.calculateIntermediate(currentEnvironment, registers, code, positionData)
        return TacNegation(toNegate)
    }

    private fun StringConcat.toTac(currentEnvironment: TacEnvironment, registers: RegisterUse, code: MutableList<TacNode>, positionData: TreePositionData): TacStringConcat {
        val str1 = this.str1.calculateIntermediate(currentEnvironment, registers, code, positionData)
        val str2 = this.str2.calculateIntermediate(currentEnvironment, registers, code, positionData)

        return TacStringConcat(str1, str2)
    }

    private fun visitSequence(
        sequence: ExpressionSequence,
        environment: TacEnvironment,
        positionData: TreePositionData,
        currentBlock: TacBlock?
    ): TacBlock {
        val newData = positionData.enterSequence(sequence)
        return visitRemainderOfSequence(environment, newData, sequence.getNodes().iterator(), currentBlock)
    }

    private fun visitRemainderOfSequence(
        environment: TacEnvironment,
        positionData: TreePositionData,
        currentSequence: Iterator<Computable>,
        currentBlock: TacBlock?
    ): TacBlock {
        val code = ArrayList<TacNode>()
        val block = TacBlock(code)

        while(currentSequence.hasNext()) {
            visitNode(currentSequence.next(), environment, code, positionData, currentSequence, currentBlock)
        }

        if(currentBlock != null && !code.any { it is TacBranch || it is TacReturn }) {
            code.add(TacGoto(currentBlock))
        }
        return block
    }

    private fun visitIfStatement(
        ifStatement: IfStatement,
        environment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData,
        currentSequence: Iterator<Computable>,
        currentBlock: TacBlock?
    ) {
        val check = ifStatement.check.calculateIntermediate(environment, RegisterUse(), code, positionData)

        val endOfCheckBlock = visitRemainderOfSequence(environment, positionData, currentSequence, currentBlock)
        val ifBlock = visitSequence(ifStatement.ifBody, environment, positionData, endOfCheckBlock)
        val elseBlock = if(ifStatement.elseBody == null) {
            endOfCheckBlock
        } else {
            visitSequence(ifStatement.elseBody!!, environment, positionData, endOfCheckBlock)
        }

        ifBlock.instructions.add(TacGoto(endOfCheckBlock))
        if(ifStatement.elseBody != null) {
            elseBlock.instructions.add(TacGoto(endOfCheckBlock))
        }
        code.add(TacBranchEqualZero(check, elseBlock, ifBlock))
    }

    private fun visitWhileLoop(
        loop: WhileLoop,
        environment: TacEnvironment,
        code: MutableList<TacNode>,
        positionData: TreePositionData,
        currentSequence: Iterator<Computable>,
        currentBlock: TacBlock?
    ) {
        val whileBlockCode = ArrayList<TacNode>()
        val whileBlock = TacBlock(whileBlockCode)
        val checkVariable = loop.check.calculateIntermediate(environment, RegisterUse(), whileBlockCode, positionData)
        val endOfLoop = visitRemainderOfSequence(environment, positionData, currentSequence, currentBlock)
        whileLoopControlBlocks.push(WhileLoopControlBlock(whileBlock, endOfLoop))

        val innerLoop = visitSequence(loop.body, environment, positionData, whileBlock)
        innerLoop.instructions.add(TacGoto(whileBlock))
        whileBlock.instructions.add(TacBranchEqualZero(checkVariable, endOfLoop, innerLoop))
        code.add(TacGoto(whileBlock))

        whileLoopControlBlocks.pop()
    }

    private fun isVariableInClosure(name: String, currentScope: ExpressionSequence): Boolean {
        for(element in currentScope.expressions) {
            if(element.findVariable(name, 0, false) != null) {
                return true
            }
        }
        return false
    }

    private fun Computable.findVariable(name: String, currentDepth: Int, isInFunction: Boolean): Computable? = when(this) {
        is VariableContext -> {
            if(this.contextIndex == currentDepth && isInFunction && this.name == name) this
            else getNodes().asSequence().map {
                it.findVariable(name, currentDepth, isInFunction)
            }.filterNotNull().firstOrNull()
        }
        is ExpressionSequence -> this.expressions.asSequence().map {
            it.findVariable(name, currentDepth + 1, isInFunction)
        }.filterNotNull().firstOrNull()
        is FunctionDeclaration -> this.body.findVariable(name, currentDepth + 1, true)
        else -> getNodes().asSequence().map {
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
}