package com.tobi.mc.intermediate

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.intermediate.code.*
import com.tobi.mc.parser.ReaderHelpers
import com.tobi.mc.util.TimeUtils
import kotlinx.coroutines.delay

class TacEmulator private constructor(private val program: TacProgram, private val environment: ExecutionEnvironment) {

    private val stack = TacStack(1)
    private val registers = TacStack(1)

    private var stackTop = 0
    private var line = 0
    private lateinit var closure: TacClosure
    private lateinit var block: TacBlock
    private var returnValue: Any? = null

    companion object {

        private const val CALLING_SPACE = 1

        suspend fun emulate(program: TacProgram, environment: ExecutionEnvironment) =  TacEmulator(program, environment).emulateStart()
    }

    private suspend fun emulateStart(): Long {
        this.closure = object : TacClosure {
            override val registersUsed: Int = 0
            override val variables: Int = 0
            override val params: Int = 0
        }

        this.line = 0
        this.block = program.mainFunction.code
        this.closure = TacFunctionClosure(program.mainFunction, Array(program.mainFunction.environment.newVariables.size) { TacData(null) })
        this.stack.ensureCapacity(CALLING_SPACE + program.mainFunction.variables.size)

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

            if(line >= block.instructions.size) {
                restoreAfterFunction()
                continue
            }

            val instruction = block.instructions[line]

            when(instruction) {
                is TacBranchEqualZero -> {
                    this.line = 0
                    if(instruction.conditionVariable.getValue() as Long == 0L) {
                        this.block = instruction.successBlock
                    } else {
                        this.block = instruction.failBlock
                    }
                    continue
                }
                is TacSetVariable -> {
                    val value = instruction.value
                    val actualValue = value.getValue()
                    instruction.variable.setValue(actualValue)
                }
                is TacSetArgument -> {
                    val index = stackTop + this.closure.params + CALLING_SPACE + this.closure.variables + instruction.index
                    stack.ensureCapacity(index + 1)
                    stack[index] = instruction.variable.getValue()
                }
                is TacGoto -> {
                    this.line = 0
                    this.block = instruction.block
                    continue
                }
                is TacReturn -> {
                    restoreAfterFunction()
                    continue
                }
                is TacFunctionCall -> {
                    val functionToCall = instruction.function.getValue() as TacClosure
                    saveForFunction(functionToCall)
                    continue
                }
                else -> throw IllegalArgumentException(instruction::class.simpleName)
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
                stack[stackTop + index]
            }
            is ReturnedValue -> this@TacEmulator.returnValue ?: throw IllegalStateException("No return value")
            is StackVariable -> {
                stack[stackTop + closure.params + CALLING_SPACE + indexOf(name, closure.code.variables)]
            }
            is EnvironmentVariable -> {
                val index = closure.code.environment.indexOf(this)
                closure.values[index].value!!
            }
            is RegisterVariable -> registers[register]
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
                INBUILT_FUNCTIONS.find {
                    it.name == this.label
                } ?: throw IllegalArgumentException("Unknown inbuilt function ${this.label}")
            }
            else -> throw IllegalArgumentException(this::class.simpleName)
        }
    }

    fun TacVariableReference.setValue(value: Any) {
        val closure = this@TacEmulator.closure as TacFunctionClosure
        when(this) {
            is StackVariable -> {
                stack[stackTop + closure.params + CALLING_SPACE + indexOf(name, closure.code.variables)] = value
            }
            is EnvironmentVariable -> {
                val index = closure.code.environment.indexOf(this)
                closure.values[index].value = value
            }
            is RegisterVariable -> {
                registers[this.register] = value
            }
            is ParamReference -> {
                stack[stackTop + index] = value
            }
            is ReturnedValue -> this@TacEmulator.returnValue = value
            else -> throw IllegalArgumentException(this::class.simpleName)
        }
    }

    private fun saveForFunction(newClosure: TacClosure) {
        stackTop += this.closure.params
        stackTop += CALLING_SPACE
        stackTop += this.closure.variables

        registers.ensureCapacity(newClosure.registersUsed)
        stack.ensureCapacity(stackTop + newClosure.params + newClosure.variables + 1)
        stack[stackTop + newClosure.params] = FrameData(this.line, this.block, this.closure, this.registers.array.copyOf())

        this.line = 0
        this.closure = newClosure
        if(newClosure is TacFunctionClosure) {
            this.block = newClosure.code.code
        }
    }

    private fun restoreAfterFunction() {
        val frameData = stack[stackTop + this.closure.params] as FrameData
        this.line = frameData.line + 1

        val registers = frameData.registers
        for(i in registers.indices) {
            this.registers[i] = registers[i]
        }
        this.closure = frameData.closure
        this.block = frameData.block

        stackTop -= this.closure.variables
        stackTop -= CALLING_SPACE
        stackTop -= this.closure.params
    }

    private fun indexOf(variable: String, variables: Set<String>): Int {
        return variables.indexOf(variable)
    }

    private interface TacClosure {
        val registersUsed: Int
        val variables: Int
        val params: Int
    }

    private class TacFunctionClosure(val code: TacFunction, val values: Array<TacData>) : TacClosure {
        override val registersUsed: Int = code.calculateRegistersUsed()
        override val variables: Int = code.variables.size
        override val params: Int = code.parameters

        override fun toString(): String = "Closure($params, $registersUsed, ${code.variables.size})"
    }
    private class InbuiltFunction(val name: String, override val params: Int, val execute: suspend () -> Any?) : TacClosure {
        override fun toString(): String = "Func<$name>"

        override val registersUsed: Int = 0
        override val variables: Int = 0
    }

    private data class FrameData(val line: Int, val block: TacBlock, val closure: TacClosure, val registers: Array<Any?>)

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

        operator fun get(index: Int) = array[index] ?: throw IndexOutOfBoundsException("$index")

        operator fun set(index: Int, value: Any?) {
            array[index] = value
        }
    }
}