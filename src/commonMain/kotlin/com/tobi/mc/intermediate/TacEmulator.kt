package com.tobi.mc.intermediate

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.intermediate.construct.TacExpression
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.parser.ReaderHelpers
import com.tobi.mc.util.TimeUtils
import kotlinx.coroutines.delay
import kotlin.math.max

class TacEmulator private constructor(private val program: TacProgram, private val environment: ExecutionEnvironment) {

    private val stack = TacStack(1)
    private val registers = TacStack(1)
    private var stackTop = 0
    private var framePointer = 0
    private var line = 0
    private var argsPushed = 0
    private lateinit var closure: TacClosure
    private var returnValue: Any? = null

    companion object {

        private const val CALLING_SPACE = 2

        suspend fun emulate(program: TacProgram, environment: ExecutionEnvironment) =  TacEmulator(program, environment).emulateStart()
    }

    private suspend fun emulateStart(): Long {
        this.closure = object : TacClosure {
            override val registersUsed: Int = 0
            override val variables: Int = 0
        }

        val closure = TacFunctionClosure(program.code, Array(program.code.environment.newVariables.size) { TacData(null) })
        saveForFunction(closure)
        return try {
            emulateCode()
            0L
        } catch (e: ExitException) {
            e.code
        }
    }

    private suspend fun emulateCode() {
        while(true) {
            if(closure is InbuiltFunction) {
                (closure as InbuiltFunction).execute()
                restoreAfterFunction()
                continue
            }

            val function = (closure as TacFunctionClosure).code
            if(line >= function.code.size) {
                restoreAfterFunction()
                continue
            }

            val structure = function.code[line]

            when(structure) {
                is TacBranchEqualZero -> {
                    if(structure.conditionVariable.getValue() as Long == 0L) {
                        line = findLabelIndex(function.code, structure.branchTo)
                        continue
                    }
                }
                is TacSetVariable -> {
                    val value = structure.value
                    val actualValue = value.getValue()
                    structure.variable.setValue(actualValue)
                }
                is TacSetArgument -> {
                    this.argsPushed = max(this.argsPushed, structure.index + 1)
                    stack.ensureCapacity(framePointer + closure.variables + CALLING_SPACE + argsPushed)
                    stack[framePointer + closure.variables + CALLING_SPACE + structure.index] = structure.variable.getValue()
                }
                is TacGoto -> {
                    line = findLabelIndex(function.code, structure.label)
                    continue
                }
                is TacReturn -> {
                    restoreAfterFunction()
                    continue
                }
                is TacFunctionCall -> {
                    val functionToCall = structure.function.getValue() as TacClosure
                    saveForFunction(functionToCall)
                    continue
                }
                is TacLabel -> {}
                else -> throw IllegalArgumentException(structure::class.simpleName)
            }
            line++
        }
    }

    fun TacExpression.getValue(): Any {
        val closure = this@TacEmulator.closure as TacFunctionClosure

        return when(this) {
            is IntValue -> value
            is StringVariable -> program.strings[stringIndex]
            is ParamReference -> {
                stack[stackTop + index]!!
            }
            is ReturnedValue -> this@TacEmulator.returnValue!!
            is StackVariable -> {
                stack[framePointer + indexOf(name, closure.code.variables)]!!
            }
            is EnvironmentVariable -> {
                val index = closure.code.environment.indexOf(this)
                closure.values[index].value!!
            }
            is RegisterVariable -> registers[register]!!
            is TacStringConcat -> (str1.getValue() as String) + (str2.getValue() as String)
            is TacUnaryMinus -> -(variable.getValue() as Long)
            is TacNegation -> {
                val value = toNegate.getValue() as Long
                if(value == 0L) 1L else 0L
            }
            is TacMathOperation -> {
                val val1 = this.arg1.getValue() as Long
                val val2 = this.arg2.getValue() as Long
                when(this.type) {
                    TacMathOperation.MathType.ADD -> val1 + val2
                    TacMathOperation.MathType.SUBTRACT -> val1 - val2
                    TacMathOperation.MathType.MULTIPLY -> val1 * val2
                    TacMathOperation.MathType.DIVIDE -> val1 / val2
                    TacMathOperation.MathType.MOD -> val1 % val2
                    TacMathOperation.MathType.GREATER_THAN -> if(val1 > val2) 1L else 0L
                    TacMathOperation.MathType.GREATER_THAN_OR_EQUAL -> if(val1 >= val2) 1L else 0L
                    TacMathOperation.MathType.LESS_THAN -> if(val1 < val2) 1L else 0L
                    TacMathOperation.MathType.LESS_THAN_OR_EQUAL -> if(val1 <= val2) 1L else 0L
                    TacMathOperation.MathType.EQUALS -> if(val1 == val2) 1L else 0L
                    TacMathOperation.MathType.NOT_EQUALS -> if(val1 != val2) 1L else 0L
                }
            }
            is TacFunction -> {
                val newVars = this.environment.newVariables.size
                val newArray = if(newVars == 0) closure.values else Array(closure.values.size + newVars) {
                    if(it < newVars) TacData(null) else closure.values[it - newVars]
                }
                TacFunctionClosure(this, newArray)
            }
            is TacInbuiltFunction -> {
                InbuiltFunction(this.label, 0, INBUILT_FUNCTIONS.find {
                    it.name == this.label
                }?.execute ?: throw IllegalArgumentException("Unknown inbuilt function ${this.label}"))
            }
            else -> throw IllegalArgumentException(this::class.simpleName)
        }
    }

    fun TacVariableReference.setValue(value: Any) {
        val closure = this@TacEmulator.closure as TacFunctionClosure
        when(this) {
            is StackVariable -> {
                stack[framePointer + indexOf(name, closure.code.variables)] = value
            }
            is EnvironmentVariable -> {
                val index = closure.code.environment.indexOf(this)
                closure.values[index].value = value
            }
            is RegisterVariable -> {
                registers[this.register] = value
            }
            is ReturnedValue -> this@TacEmulator.returnValue = value
            else -> throw IllegalArgumentException(this::class.simpleName)
        }
    }

    private fun saveForFunction(newClosure: TacClosure) {
        /*
            closure reference
            line number
            sp - [args pushed]
            count of args pushed
            [saved registers]
            [new frame stack variables]
            closure reference
            line number
            sp - [args pushed]
            count of args pushed
            [saved registers]
            fp -- [new frame stack variables]
         */
        stackTop = framePointer + this.closure.variables
        stack.ensureCapacity(stackTop + CALLING_SPACE + argsPushed + 1 + newClosure.registersUsed + newClosure.variables)
        registers.ensureCapacity(newClosure.registersUsed)

        stack[stackTop++] = this.closure
        stack[stackTop++] = this.line
        stack[stackTop + argsPushed] = this.argsPushed

        framePointer = stackTop + this.argsPushed + 1


        for(i in 0 until newClosure.registersUsed) {
            stack[framePointer++] = registers[i]
        }

        this.line = 0
        this.argsPushed = 0
        this.closure = newClosure
    }

    private fun restoreAfterFunction() {
        framePointer--
        framePointer -= closure.registersUsed
        for(i in 0 until closure.registersUsed) {
            registers[i] = stack[framePointer + i]
        }

        this.argsPushed = this.stack[framePointer--] as Int
        this.framePointer -= this.argsPushed
        this.line = this.stack[framePointer--] as Int + 1

        this.closure = this.stack[framePointer] as TacClosure
        framePointer -= this.closure.variables

        this.stackTop = framePointer - this.closure.registersUsed
        this.stackTop -= (this.stack[stackTop - 1] as Int) + 1
        this.argsPushed = 0
    }

    private fun findLabelIndex(code: List<TacStructure>, label: String): Int {
        return code.indexOfFirst { it is TacLabel && it.label == label }
    }

    private fun indexOf(variable: String, variables: Set<String>): Int {
        return variables.indexOf(variable)
    }

    private interface TacClosure {
        val registersUsed: Int
        val variables: Int
    }

    private class TacFunctionClosure(val code: TacFunction, val values: Array<TacData>) : TacClosure {
        override val registersUsed: Int
            get() = code.registersUsed
        override val variables: Int
            get() = code.variables.size

        override fun toString(): String = "Closure($registersUsed, ${code.variables})"
    }
    private class InbuiltFunction(val name: String, val params: Int, val execute: suspend () -> Any?) : TacClosure {
        override fun toString(): String = "Func<$name>"

        override val registersUsed: Int = 0
        override val variables: Int = 0
    }

    private data class TacData(var value: Any?)

    private val INBUILT_FUNCTIONS = listOf(
        InbuiltFunction("printInt", 1) {
            environment.print(stack[stackTop].toString())
        },
        InbuiltFunction("printString", 1) {
            environment.print(stack[stackTop].toString())
        },
        InbuiltFunction("readInt", 0) {
            this.returnValue = readInt()
            Unit
        },
        InbuiltFunction("readString", 0) {
            this.returnValue = environment.readLine()
            Unit
        },
        InbuiltFunction("intToString", 1) {
            this.returnValue = stack[stackTop].toString()
            Unit
        },
        InbuiltFunction("concat", 2) {
            this.returnValue = stack[stackTop].toString() + stack[stackTop + 1].toString()
            Unit
        },
        InbuiltFunction("unixTime", 0) {
            this.returnValue = TimeUtils.unixTimeMillis()
            Unit
        },
        InbuiltFunction("sleep", 1) {
            delay(stack[stackTop] as Long)
        },
        InbuiltFunction("exit", 1) {
            throw ExitException(stack[stackTop] as Long)
        }
    )

    private suspend fun readInt(): Long {
        var result: Long? = null
        while(result == null) {
            result = environment.readLine().toLongOrNull()
            if(result == null) environment.println("Invalid integer. Enter again:")
        }
        return result
    }

    private class ExitException(val code: Long) : RuntimeException()

    private class TacStack(initialSize: Int) {

        var array: Array<Any?> = arrayOfNulls(initialSize)

        fun ensureCapacity(size: Int) {
            if(array.size < size) {
                var newSize = array.size
                while(newSize < size) {
                    newSize *= 2
                }
                this.array = ReaderHelpers.expandArray(this.array, newSize)
            }
        }

        operator fun get(index: Int) = array[index]
        operator fun set(index: Int, value: Any?) {
            array[index] = value
        }
    }
}