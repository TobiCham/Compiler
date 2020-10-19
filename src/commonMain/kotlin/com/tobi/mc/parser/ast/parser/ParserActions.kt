package com.tobi.mc.parser.ast.parser

import com.tobi.mc.ParseException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.control.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.Parameter
import com.tobi.mc.computable.operation.*
import com.tobi.mc.computable.variable.DefineVariable
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.parser.ast.parser.runtime.LRParser
import com.tobi.mc.parser.ast.parser.runtime.Symbol
import com.tobi.mc.util.Stack

class ParserActions(
    private val parser: Parser,
    private val defaultContext: Context
) {

    private fun throwException(message: String, stackIndex: Int): Nothing {
        throwException(message, stackIndex, parser.stack.size - 1)
    }

    private fun throwException(message: String, start: Int, end: Int): Nothing {
        val startSymbol = parser.stack[start]
        val endSymbol = parser.stack[end]
        throw ParseException(message, SourceRange(startSymbol.startPosition!!, endSymbol.endPosition!!))
    }

    private fun isString(computable: Computable?): Boolean {
        return computable is DataTypeString || computable is StringConcat || computable is GetVariable || computable is FunctionCall
    }

    private fun isMath(computable: Computable?): Boolean {
        return computable is DataTypeInt || computable is MathOperation || computable is UnaryMinus || computable is Negation || computable is GetVariable || computable is FunctionCall
    }

    private fun isCallable(computable: Computable?): Boolean {
        return computable is GetVariable || computable is FunctionCall
    }

    private fun <T : MathOperation> createMath(
        stackTop: Int,
        arg1: Computable,
        arg2: Computable,
        createMath: (Computable, Computable) -> T
    ): T {
        if (!isMath(arg1)) throwException("Invalid math expression", stackTop - 2, stackTop - 2)
        if (!isMath(arg2)) throwException("Invalid math expression", stackTop, stackTop)
        return createMath(arg1, arg2)
    }

    private fun createMath(arg: Computable, createMath: (Computable) -> Computable): Computable {
        if (!isMath(arg)) throwException("Invalid math expression", parser.stack.size - 1)
        return createMath(arg)
    }

    private fun <T : MathOperation> makeMath(stackTop: Int, createMath: (Computable, Computable) -> T): Symbol {
        val stack = this.parser.stack
        val arg1 = stack[stackTop - 2].value as Computable
        val arg2 = stack.peek().value as Computable
        val RESULT = createMath(stackTop, arg1, arg2, createMath)
        return this.parser.symbolFactory.newSymbol("mathComputable", 12, stack[stackTop - 2], stack.peek(), RESULT)
    }

    private fun makeDataType(type: DataType?) = makeSimple("dataType", 0, type)

    private fun makeSimple(name: String, type: Int, value: Any?): Symbol {
        val symbol = parser.stack.peek()
        return this.parser.symbolFactory.newSymbol(name, type, symbol, symbol, value)
    }

    private fun makeExisting(name: String, type: Int): Symbol {
        val symbol = parser.stack.peek()
        return this.parser.symbolFactory.newSymbol(name, type, symbol, symbol, symbol.value)
    }

    fun doAction(actionId: Int, parser: LRParser, stack: Stack<Symbol>, stackTop: Int) = when (actionId) {
        0 -> {
            val program = stack[stackTop - 1].value
            val result = this.parser.symbolFactory.newSymbol("\$START", 0, stack[stackTop - 1], stack.peek(), program)
            parser.done_parsing()
            result
        }
        1 -> {
            val list = stack.peek().value as List<Computable>
            val expression = ExpressionSequence(list)
            val program = Program(expression, defaultContext)
            val symbol = this.parser.symbolFactory.newSymbol("program", 22, stack.peek(), stack.peek(), program)
            expression.sourceRange = program.sourceRange
            symbol
        }
        2 -> makeDataType(DataType.STRING)
        3 -> makeDataType(DataType.INT)
        4 -> makeDataType(DataType.FUNCTION)
        5 -> makeDataType(DataType.VOID)
        6 -> makeDataType(null)
        7 -> {
            val value = stack.peek().value as Long
            val RESULT = DataTypeInt(value)
            this.parser.symbolFactory.newSymbol("integer", 1, stack.peek(), stack.peek(), RESULT)
        }
        8 -> {
            val value = stack.peek().value as String
            val RESULT = DataTypeString(value)
            this.parser.symbolFactory.newSymbol("string", 2, stack.peek(), stack.peek(), RESULT)
        }
        9 -> {
            val typeSymbol = stack[stackTop - 1]
            val type = typeSymbol.value as DataType?
            val name = stack.peek().value as String

            if (type === DataType.VOID) throwException("Can't define a parameter type as void", stackTop - 1, stackTop - 1)
            else if (type == null) throwException("Can't define a parameter type as auto", stackTop - 1, stackTop - 1)

            val RESULT = Parameter(type, name)
            this.parser.symbolFactory.newSymbol("parameter", 3, stack[stackTop - 1], stack.peek(), RESULT)
        }
        10 -> {
            val param = stack.peek().value as Parameter
            val RESULT: MutableList<Parameter> = ArrayList()
            RESULT.add(param)
            this.parser.symbolFactory.newSymbol("functionParams", 4, stack.peek(), stack.peek(), RESULT)
        }
        11 -> {
            val params = stack[stackTop - 2].value as MutableList<Parameter>
            val param = stack.peek().value as Parameter
            params.add(param)
            this.parser.symbolFactory.newSymbol("functionParams", 4, stack[stackTop - 2], stack.peek(), params)
        }
        12 -> {
            val name = stack.peek().value as String
            val RESULT = GetVariable(name, -1)
            this.parser.symbolFactory.newSymbol("getVariable", 7, stack.peek(), stack.peek(), RESULT)
        }
        13 -> {
            val name = stack[stackTop - 2].value as String
            val assignment = stack.peek().value as Computable
            val RESULT = SetVariable(name, -1, assignment)
            this.parser.symbolFactory.newSymbol("setVariable", 8, stack[stackTop - 2], stack.peek(), RESULT)
        }
        14 -> {
            val type = stack[stackTop - 3].value as DataType?
            val name = stack[stackTop - 2].value as String
            val value = stack.peek().value as Computable
            if (type === DataType.VOID) {
                throwException("Cannot define variables as void", stackTop - 3, stackTop - 3)
            }
            val RESULT = DefineVariable(name, value, type)
            this.parser.symbolFactory.newSymbol("defineVariable", 9, stack[stackTop - 3], stack.peek(), RESULT)
        }
        15 -> {
            val type = stack[stackTop - 1].value as DataType?
            val name = stack.peek().value as String
            if (type == null) {
                throwException("Cannot define late init variables as auto", stackTop - 1, stackTop - 1)
            }
            throwException("Late init variables are not yet supported", stackTop - 1)
//                this.parser.symbolFactory.newSymbol("defineVariable", 9, stack.get(stackTop - 1), stack.peek(), RESULT)
        }
        16 -> {
            val RESULT = stack[stackTop - 1].value as Computable
            this.parser.symbolFactory.newSymbol("Computable", 11, stack[stackTop - 2], stack.peek(), RESULT)
        }
        17, 18, 19 -> makeExisting("Computable", 11)
        20 -> {
            val exp1 = stack[stackTop - 2].value as Computable
            val exp2 = stack.peek().value as Computable
            if (!isString(exp1)) throwException("Can only concatenate strings", stackTop - 2, stackTop - 2)
            if (!isString(exp2)) throwException("Can only concatenate strings", stackTop)
            val RESULT = StringConcat(exp1, exp2)
            this.parser.symbolFactory.newSymbol("Computable", 11, stack[stackTop - 2], stack.peek(), RESULT)
        }
        21, 22 -> makeExisting("mathComputable", 12)
        23 -> {
            val math = stack.peek().value as Computable
            val RESULT = createMath(math, ::UnaryMinus)
            this.parser.symbolFactory.newSymbol("mathComputable", 12, stack[stackTop - 1], stack.peek(), RESULT)
        }
        24 -> {
            val math = stack.peek().value as Computable
            val RESULT = createMath(math, ::Negation)
            this.parser.symbolFactory.newSymbol("mathComputable", 12, stack[stackTop - 1], stack.peek(), RESULT)
        }
        25 -> makeMath(stackTop, ::Add)
        26 -> makeMath(stackTop, ::Subtract)
        27 -> makeMath(stackTop, ::Multiply)
        28 -> makeMath(stackTop, ::Divide)
        29 -> makeMath(stackTop, ::Mod)
        30 -> makeMath(stackTop, ::Equals)
        31 -> makeMath(stackTop, ::NotEquals)
        32 -> makeMath(stackTop, ::LessThan)
        33 -> makeMath(stackTop, ::GreaterThan)
        34 -> makeMath(stackTop, ::LessThanOrEqualTo)
        35 -> makeMath(stackTop, ::GreaterThanOrEqualTo)
        36 -> {
            val func = stack[stackTop - 3].value as Computable
            val args = stack[stackTop - 1].value as List<Computable>
            if (!isCallable(func)) throwException("Not callable", stackTop - 3)
            val RESULT = FunctionCall(func, args.toTypedArray())
            this.parser.symbolFactory.newSymbol("functionCall", 10, stack[stackTop - 3], stack.peek(), RESULT)
        }
        37 -> {
            val func = stack[stackTop - 2].value as Computable
            if (!isCallable(func)) throwException("Not callable", stackTop - 2)
            val RESULT = FunctionCall(func, emptyArray())
            this.parser.symbolFactory.newSymbol("functionCall", 10, stack[stackTop - 2], stack.peek(), RESULT)
        }
        38 -> {
            val arg = stack.peek().value as Computable
            val RESULT: MutableList<Computable> = ArrayList()
            RESULT.add(arg)
            this.parser.symbolFactory.newSymbol("functionArgs", 6, stack.peek(), stack.peek(), RESULT)
        }
        39 -> {
            val args = stack[stackTop - 2].value as MutableList<Computable>
            val arg = stack.peek().value as Computable
            args.add(arg)
            this.parser.symbolFactory.newSymbol("functionArgs", 6, stack[stackTop - 2], stack.peek(), args)
        }
        40 -> {
            val returnType = stack[stackTop - 5].value as DataType?
            val name = stack[stackTop - 4].value as String
            val params = stack[stackTop - 2].value as List<Parameter>
            val body = stack.peek().value as ExpressionSequence
            val RESULT = FunctionDeclaration(name, params, body, returnType)
            this.parser.symbolFactory.newSymbol("functionDeclaration", 5, stack[stackTop - 5], stack.peek(), RESULT)
        }
        41 -> {
            val returnType = stack[stackTop - 4].value as DataType?
            val name = stack[stackTop - 3].value as String
            val body = stack.peek().value as ExpressionSequence
            val RESULT = FunctionDeclaration(name, ArrayList(), body, returnType)
            this.parser.symbolFactory.newSymbol("functionDeclaration", 5, stack[stackTop - 4], stack.peek(), RESULT)
        }
        42, 43, 44, 45, 46, 47 -> makeExisting("terminatedExpression", 14)
        48 -> {
            val RESULT = stack[stackTop - 1].value as Computable
            this.parser.symbolFactory.newSymbol("expression", 13, stack[stackTop - 1], stack.peek(), RESULT)
        }
        49, 50, 51 -> makeExisting("expression", 13)
        52 -> {
            val exp = stack.peek().value as Computable
            val RESULT: MutableList<Computable> = ArrayList()
            RESULT.add(exp)
            this.parser.symbolFactory.newSymbol("expressionList", 16, stack.peek(), stack.peek(), RESULT)
        }
        53 -> {
            val list = stack[stackTop - 1].value as MutableList<Computable>
            val exp = stack.peek().value as Computable
            list.add(exp)
            this.parser.symbolFactory.newSymbol("expressionList", 16, stack[stackTop - 1], stack.peek(), list)
        }
        54 -> {
            val RESULT = ExpressionSequence(ArrayList())
            this.parser.symbolFactory.newSymbol("expressionSequence", 15, stack[stackTop - 1], stack.peek(), RESULT)
        }
        55 -> {
            val list = stack[stackTop - 1].value as List<Computable>
            val RESULT = ExpressionSequence(list)
            this.parser.symbolFactory.newSymbol("expressionSequence", 15, stack[stackTop - 2], stack.peek(), RESULT)
        }
        56 -> {
            val exp = stack.peek().value as Computable
            val list: MutableList<Computable> = ArrayList()
            list.add(exp)
            val RESULT = ExpressionSequence(list)
            this.parser.symbolFactory.newSymbol("expressionSequence", 15, stack.peek(), stack.peek(), RESULT)
        }
        57 -> {
            val condition = stack[stackTop - 2].value as Computable
            val body = stack.peek().value as ExpressionSequence
            if (!isMath(condition)) throwException("Invalid condition", stackTop - 2)
            val RESULT = WhileLoop(condition, body)
            this.parser.symbolFactory.newSymbol("whileLoop", 20, stack[stackTop - 4], stack.peek(), RESULT)
        }
        58 -> {
            val condition = stack[stackTop - 4].value as Computable
            val body = stack[stackTop - 2].value as ExpressionSequence
            val elseBody = stack.peek().value as ExpressionSequence
            if (!isMath(condition)) throwException("Invalid condition", stackTop - 4)
            val ifStatement = IfStatement(condition, body, elseBody)

            this.parser.symbolFactory.newSymbol("ifStatement", 21, stack[stackTop - 6], stack.peek(), ifStatement)
        }
        59 -> {
            val condition = stack[stackTop - 2].value as Computable
            val body = stack.peek().value as ExpressionSequence
            if (!isMath(condition)) throwException("Invalid condition", stackTop - 2)
            val RESULT = IfStatement(condition, body, null)
            this.parser.symbolFactory.newSymbol("ifStatement", 21, stack[stackTop - 4], stack.peek(), RESULT)
        }
        60 -> {
            val toReturn = stack.peek().value as Computable
            val RESULT = ReturnStatement(toReturn)
            this.parser.symbolFactory.newSymbol("returnExpression", 19, stack[stackTop - 1], stack.peek(), RESULT)
        }
        61 -> makeSimple("returnExpression", 19, ReturnStatement(null))
        62 -> makeSimple("breakStatement", 18, BreakStatement())
        63 -> makeSimple("continueStatement", 17, ContinueStatement())
        else -> throw Exception("Invalid action number " + actionId + "found in internal parse table")
    }
}