package com.tobi.mc.mips

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacExpression
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.*

class FunctionToMips private constructor(
    private val functionName: String,
    private val function: TacFunction,
    val config: MipsConfiguration,
    private val programBuilder: MipsProgram.Builder
) {

    val builder = MipsFunction.Builder("main")
    private val endFuncLabel = "${this.functionName}_end"

    private val excessRegesterStackLocs = HashMap<Int, Int>()

    companion object {

        const val PROCEDURE_PUSH_ARG = "pushArg"
        const val PROCEDURE_CALL_CLOSURE = "callClosure"
        const val REMOVE_CLOSURE_FRAME = "removeClosureFrame"
        const val PROCEDURE_CREATE_CLOSURE = "createClosure"

        private const val RESERVED_REGISTERS = 3

        fun toMips(functionName: String, function: TacFunction, config: MipsConfiguration, programBuilder: MipsProgram.Builder): MipsFunction {
            val instance = FunctionToMips(functionName, function, config, programBuilder)
            for (construct in function.code) {
                instance.addConstruct(construct)
            }

            val registers = instance.findRegistersUsed(instance.builder.instructions)
            val stackVariables = function.variables.size + instance.excessRegesterStackLocs.size
            val environmentVariables = function.environment.newVariables.size

            val storeRegistersInstructions = instance.createStoreRegistersInstructions(registers)
            val restoreRegistersInstructions = instance.createRestoreRegistersInstructions(registers)

            val newInstructions = ArrayList<MipsInstruction>()
            newInstructions.addAll(storeRegistersInstructions)
            newInstructions.addAll(instance.builder.instructions)
            newInstructions.add(MipsInstruction("${instance.endFuncLabel}:"))
            newInstructions.addAll(restoreRegistersInstructions)
            newInstructions.add(MipsInstruction("b", MipsArgument.Label(REMOVE_CLOSURE_FRAME)))

            val newFunction = MipsFunction(functionName, newInstructions, stackVariables, registers.size, environmentVariables)
            programBuilder.addFunction(newFunction)

            return newFunction
        }
    }

    private fun createRegistersInstructions(registers: Set<String>, instruction: String): List<MipsInstruction> {
        val instructions = ArrayList<MipsInstruction>()
        var offset = 4
        for(register in registers) {
            instructions.add(MipsInstruction(
                instruction,
                MipsArgument.Register(register),
                MipsArgument.IndirectRegister(config.framePointer, offset)
            ))
            offset += 4
        }
        return instructions
    }

    private fun createStoreRegistersInstructions(registers: Set<String>): List<MipsInstruction> {
        return createRegistersInstructions(registers, "sw")
    }

    private fun createRestoreRegistersInstructions(registers: Set<String>): List<MipsInstruction> {
        return createRegistersInstructions(registers, "lw")
    }

    private fun findRegistersUsed(instructions: List<MipsInstruction>): Set<String> {
        val registers = HashSet<String>()
        for (instruction in instructions) {
            for(arg in instruction.args) {
                if(arg is MipsArgument.Register && config.temporaryRegisters.contains(arg.name)) {
                    registers.add(arg.name)
                } else if(arg is MipsArgument.IndirectRegister  && config.temporaryRegisters.contains(arg.name)) {
                    registers.add(arg.name)
                }
            }
        }
        return registers
    }

    private fun addConstruct(construct: TacStructure) {
        when(construct) {
            is ConstructPushArgument -> {
                copyToRegister(MipsArgument.Register(config.argumentRegisters.first()), construct.variable)
                builder.add(MipsInstruction("jal", MipsArgument.Label(PROCEDURE_PUSH_ARG)))
            }
            is ConstructPopArgument -> {
                val register = MipsArgument.Register(config.argsPushedRegister)
                builder.add(MipsInstruction("addi", register, register, MipsArgument.Value(-4)))
            }
            is ConstructFunctionCall -> handleFunction(construct)
            is ConstructSetVariable -> {
                val variable = construct.variable
                when(variable) {
                    is RegisterVariable -> {
                        if(isValidRegister(variable.register)) {
                            copyToRegister(MipsArgument.Register(config.temporaryRegisters[variable.register]), construct.value)
                        } else {
                            val register = allocateRegister(construct.value, config.temporaryRegisters.last())
                            builder.add(MipsInstruction("sw", register, getExcessRegisterRegister(variable.register)))
                        }
                    }
                    is StackVariable -> {
                        val register = allocateRegister(construct.value, config.temporaryRegisters.last())
                        builder.add(MipsInstruction("sw", register, getVariableRegister(variable.name)))
                    }
                    is EnvironmentVariable -> {
                        val valueRegister = allocateRegister(construct.value, config.temporaryRegisters.last())
                        val memLocationRegister = config.temporaryRegisters[config.temporaryRegisters.size - 2]
                        val index = function.environment.indexOf(variable)
                        builder.add(MipsInstruction(
                            "lw",
                            MipsArgument.Register(memLocationRegister),
                            MipsArgument.IndirectRegister(config.currentEnvironmentRegister, index * 4)
                        ))
                        builder.add(MipsInstruction(
                            "sw", valueRegister,
                            MipsArgument.IndirectRegister(memLocationRegister, 0)
                        ))
                    }
                    is ReturnedValue -> {
                        copyToRegister(MipsArgument.Register(config.returnRegister), construct.value)
                    }
                }
            }
            is ConstructBranchEqualZero -> {
                val register = allocateRegister(construct.conditionVariable, config.temporaryRegisters.last())
                builder.add(MipsInstruction("beq", register, MipsArgument.Register(config.zeroRegister), MipsArgument.Label(construct.branchLabel)))
            }
            is ConstructGoto -> {
                builder.add(MipsInstruction("j", MipsArgument.Label(construct.label)))
            }
            is ConstructLabel -> {
                builder.add(MipsInstruction("${construct.label}:"))
            }
            is ConstructReturn -> {
                builder.add(MipsInstruction("j", MipsArgument.Label(endFuncLabel)))
            }
            else -> throw IllegalStateException(construct::class.simpleName)
        }
    }

    private fun getVariableRegister(name: String): MipsArgument.IndirectRegister {
        return MipsArgument.IndirectRegister(config.framePointer, getFrameOffset(name) * -4)
    }

    private fun getFrameOffset(name: String): Int {
        val index = this.function.variables.keys.indexOf(name)
        if(index < 0) {
            throw IllegalArgumentException("Variable $name not found")
        }
        return index
    }

    private fun handleFunction(call: ConstructFunctionCall) {
        copyToRegister(MipsArgument.Register(config.argumentRegisters.first()), call.function)
        builder.add(MipsInstruction("jal", MipsArgument.Label(PROCEDURE_CALL_CLOSURE)))
    }

    private fun copyToRegister(register: MipsArgument, expression: TacExpression) {
        val finalInstruction = expression.getCopyToRegisterInstruction()
        builder.add(MipsInstruction(finalInstruction.instruction, register, *finalInstruction.args.toTypedArray()))
    }

    private fun allocateRegister(expression: TacExpression, defaultRegister: String): MipsArgument.Register {
        if(expression is RegisterVariable && isValidRegister(expression.register)) {
            return MipsArgument.Register(config.temporaryRegisters[expression.register])
        }
        if(expression is EnvironmentVariable) {
            val index = function.environment.indexOf(expression)
            val tempRegister = MipsArgument.Register(defaultRegister)
            builder.add(MipsInstruction("lw", tempRegister, MipsArgument.IndirectRegister(config.currentEnvironmentRegister, index * 4)))
            builder.add(MipsInstruction("lw", tempRegister, MipsArgument.IndirectRegister(tempRegister.name, 0)))
            return MipsArgument.Register(defaultRegister)
        }
        copyToRegister(MipsArgument.Register(defaultRegister), expression)
        return MipsArgument.Register(defaultRegister)
    }

    private fun TacExpression.getCopyToRegisterInstruction() = when(this) {
        is StringVariable -> MipsInstruction("la", MipsArgument.Label("STRING${stringIndex}"))
        is IntValue -> {
            if(this.value > Int.MAX_VALUE || this.value < Int.MIN_VALUE) {
                throw IllegalArgumentException("Value of ${this.value} is larger than the maximum mips integer size")
            }
            MipsInstruction("li", MipsArgument.Value(value.toInt()))
        }
        is RegisterVariable -> {
            if(isValidRegister(this.register)) {
                MipsInstruction("move", MipsArgument.Register(config.temporaryRegisters[register]))
            } else {
                MipsInstruction("lw", getExcessRegisterRegister(register))
            }
        }
        is ReturnedValue -> MipsInstruction("move", MipsArgument.Register(config.returnRegister))
        is StackVariable -> MipsInstruction("lw", getVariableRegister(name))
        is EnvironmentVariable -> {
            val index = function.environment.indexOf(this)
            val tempRegister = MipsArgument.Register(config.temporaryRegisters.last())
            builder.add(MipsInstruction("lw", tempRegister, MipsArgument.IndirectRegister(config.currentEnvironmentRegister, index * 4)))
            MipsInstruction("lw", MipsArgument.IndirectRegister(tempRegister.name, 0))
        }
        is ConstructNegation -> {
            copyToRegister(MipsArgument.Register(config.argumentRegisters[0]), toNegate)
            MipsInstruction("neg", MipsArgument.Register(config.argumentRegisters[0]))
        }
        is ParamReference -> MipsInstruction("lw", MipsArgument.IndirectRegister(config.stackPointer, this.index * -4))
        is ConstructMath -> {
            val r1 = allocateRegister(arg1, config.temporaryRegisters[config.temporaryRegisters.size - 2])
            val r2 = allocateRegister(arg2, config.temporaryRegisters[config.temporaryRegisters.size - 3])

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
            val register = allocateRegister(variable, config.argumentRegisters[0])
            MipsInstruction("sub", MipsArgument.Register(config.zeroRegister), register)
        }
        is TacFunction -> {
            val newClosureMeta = toMips("${this@FunctionToMips.functionName}_${this.codeName}", this, config, programBuilder)
            builder.add(MipsInstruction("la", MipsArgument.Register(config.argumentRegisters[0]), MipsArgument.Label(newClosureMeta.label)))
            builder.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[1]), MipsArgument.Value(newClosureMeta.stackVariables)))
            builder.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[2]), MipsArgument.Value(newClosureMeta.registers)))
            builder.add(MipsInstruction("li", MipsArgument.Register(config.argumentRegisters[3]), MipsArgument.Value(newClosureMeta.environmentVariables)))
            builder.add(MipsInstruction("jal", MipsArgument.Label(PROCEDURE_CREATE_CLOSURE)))

            MipsInstruction("move", MipsArgument.Register(config.resultRegister))
        }
        else -> throw IllegalStateException(this::class.simpleName)
    }

    private fun isValidRegister(register: Int): Boolean {
        return register < config.temporaryRegisters.size - RESERVED_REGISTERS
    }

    private fun getExcessRegisterRegister(register: Int): MipsArgument.IndirectRegister {
        val offsetIndex = this.excessRegesterStackLocs.getOrPut(register) {
            register - (config.temporaryRegisters.size - RESERVED_REGISTERS) + function.variables.size
        }
        return MipsArgument.IndirectRegister(config.framePointer, offsetIndex * - 4)
    }
}