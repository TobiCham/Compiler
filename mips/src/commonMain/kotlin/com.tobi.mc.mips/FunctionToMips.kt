package com.tobi.mc.mips

import com.tobi.mc.intermediate.construct.TacExpression
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.*

class FunctionToMips private constructor(function: TacFunction, val config: MipsConfiguration) {

    val builder = MipsFunction.Builder(function.name)

    companion object {
        fun toMips(function: TacFunction, config: MipsConfiguration): MipsFunction {
            val instance = FunctionToMips(function, config)
            for (construct in function.code) {
                instance.addConstruct(construct)
            }
            return instance.builder.build()
        }
    }

    private var argsUsed = 0
    private var temporaryUsed = 0

    private fun addConstruct(construct: TacCodeConstruct) {
        when(construct) {
            is ConstructPushArgument -> {
                if(argsUsed >= config.argumentRegisters.size) {
                    throw IllegalStateException("Too many argument registers have been used")
                }
                copyToRegister(config.argumentRegisters[argsUsed], construct.variable)
                argsUsed++
            }
            is ConstructPopArgument -> {
                argsUsed--
                if(argsUsed < 0) throw IllegalStateException("Invalid TAC - too many pop argument statements")
            }
            is ConstructFunctionCall -> handleFunction(construct)
            is ConstructSetVariable -> {
                val variable = construct.variable
                when(variable) {
                    is RegisterVariable -> copyToRegister(config.temporaryRegisters[variable.register], construct.value)
                    else -> throw IllegalStateException(variable::class.simpleName)
                }
            }
            else -> throw IllegalStateException(construct::class.simpleName)
        }
    }

    private fun handleFunction(call: ConstructFunctionCall) {
        //Temporary assumption to test with
        val ref = call.function as EnvironmentVariable
        when(ref.name) {
            "printString" -> sysCall(SysCallType.PRINT_STRING)
            "readString" -> sysCall(SysCallType.READ_STRING)
            "printInt" -> sysCall(SysCallType.PRINT_INT)
            "readInt" -> sysCall(SysCallType.READ_INT)
            "exit" -> sysCall(SysCallType.EXIT)
            else -> throw IllegalStateException(ref.name)
        }
    }

    private fun copyToRegister(register: String, expression: TacExpression) {
        val finalInstruction = expression.getCopyToRegisterInstruction()
        builder.add(MipsInstruction(finalInstruction.instruction, MipsArgument.Register(register), *finalInstruction.args.toTypedArray()))
    }

    private fun TacExpression.getCopyToRegisterInstruction() = when(this) {
        is StringVariable -> MipsInstruction("la", MipsArgument.Label("STRING${stringIndex}"))
        is IntValue -> MipsInstruction("li", MipsArgument.Value(value))
        is RegisterVariable -> MipsInstruction("move", MipsArgument.Register(config.temporaryRegisters[register]))
        is ConstructNegation -> {
            copyToRegister(config.argumentRegisters[0], toNegate)
            MipsInstruction("neg", MipsArgument.Register(config.argumentRegisters[0]))
        }
        is ConstructMath -> {
            copyToRegister(config.argumentRegisters[0], arg1)
            copyToRegister(config.argumentRegisters[1], arg2)
            val r1 = MipsArgument.Register(config.argumentRegisters[0])
            val r2 = MipsArgument.Register(config.argumentRegisters[0])

            when(type) {
                ConstructMath.MathType.ADD -> MipsInstruction("add", r1, r2)
                ConstructMath.MathType.SUBTRACT -> MipsInstruction("sub", r1, r2)
                ConstructMath.MathType.MULTIPLY -> MipsInstruction("mul", r1, r2)
                ConstructMath.MathType.DIVIDE -> MipsInstruction("div", r1, r2)
                ConstructMath.MathType.MOD -> MipsInstruction("rem", r1, r2)
                ConstructMath.MathType.EQUALS -> MipsInstruction("seq", r1, r2)
                ConstructMath.MathType.NOT_EQUALS -> {
                    builder.add(MipsInstruction("seq", r1, r1, r2))
                    MipsInstruction("neg", r1)
                }
                ConstructMath.MathType.LESS_THAN -> MipsInstruction("slt", r1, r2)
                ConstructMath.MathType.LESS_THAN_OR_EQUAL -> MipsInstruction("sle", r1, r2)
                ConstructMath.MathType.GREATER_THAN -> MipsInstruction("sgt", r1, r2)
                ConstructMath.MathType.GREATER_THAN_OR_EQUAL -> MipsInstruction("sge", r1, r2)
            }
        }
        is ConstructUnaryMinus -> {
            copyToRegister(config.argumentRegisters[0], variable)
            MipsInstruction("sub", MipsArgument.Register(config.zeroRegister), MipsArgument.Register(config.argumentRegisters[0]))
        }
        is ConstructFunctionCall -> {
            handleFunction(this)
            MipsInstruction("move", MipsArgument.Register(config.returnRegister))
        }
        else -> throw IllegalStateException(this::class.simpleName)
    }

    private fun sysCall(type: SysCallType) {
        builder.add(MipsInstruction("li", MipsArgument.Register(config.sysCallRegister), MipsArgument.Value(config.getSysCallCode(type).toLong())))
        builder.add(MipsInstruction("syscall"))
    }
}