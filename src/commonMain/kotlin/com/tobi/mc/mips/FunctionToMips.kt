package com.tobi.mc.mips

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacExpression
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.TacInbuiltFunction
import com.tobi.mc.intermediate.construct.code.*
import com.tobi.mc.util.addAll

class FunctionToMips private constructor(
    private val functionName: String,
    private val function: TacFunction,
    val config: MipsConfiguration,
    private val programBuilder: MipsProgram.Builder
) {

    val builder = MipsFunction.Builder("main")
    private val endFuncLabel = "${this.functionName}_end"
    private val registerMapping = RegisterMapping(function, config.temporaryRegisters.size - RESERVED_REGISTERS)
    private val frameSize =
            FUNCTION_DATA_SIZE +
            RESERVED_REGISTERS + registerMapping.physicalRegistersCount +
            this.function.variables.size + registerMapping.excessRegisters

    companion object {

        const val PROCEDURE_CREATE_CLOSURE = "createClosure"
        const val FUNCTION_DATA_SIZE = 4

        private const val RESERVED_REGISTERS = 3

        fun toMips(functionName: String, function: TacFunction, config: MipsConfiguration, programBuilder: MipsProgram.Builder): MipsFunction {
            val instance = FunctionToMips(functionName, function, config, programBuilder)
            for (construct in function.code) {
                instance.addConstruct(construct)
            }

            val stackVariables = function.variables.size + instance.registerMapping.excessRegisters
            val environmentVariables = function.environment.newVariables.size
            val registers = function.registersUsed + instance.registerMapping.physicalRegistersCount

            val registersToSave = LinkedHashSet<String>()
            registersToSave.addAll(config.closureRegister, config.currentEnvironmentRegister, config.returnAddressRegister, config.framePointer)
            for(i in 0 until RESERVED_REGISTERS) {
                registersToSave.add(config.temporaryRegisters[config.temporaryRegisters.size - i - 1])
            }
            registersToSave.addAll(instance.registerMapping.physicalRegisters.map { registerId -> config.temporaryRegisters[registerId] })

            val newInstructions = ArrayList<MipsInstruction>()
            newInstructions.addAll(instance.createRegisterInstructions(registersToSave, "sw", config.stackPointer))
            newInstructions.addAll(
                MipsInstruction("addi", Register(config.stackPointer), Register(config.stackPointer), Value(-4 * instance.frameSize)),
                MipsInstruction("move", Register(config.framePointer), Register(config.stackPointer)),
                MipsInstruction("move", Register(config.closureRegister), Register(config.argumentRegisters[0])),
                MipsInstruction("lw", Register(config.currentEnvironmentRegister), IndirectRegister(config.closureRegister, 8))
            )
            newInstructions.addAll(instance.builder.instructions)
            newInstructions.add(MipsInstruction("${instance.endFuncLabel}:"))
            newInstructions.add(MipsInstruction("addi", Register(config.stackPointer), Register(config.stackPointer), Value(4 * instance.frameSize)))
            newInstructions.addAll(instance.createRegisterInstructions(registersToSave, "lw", config.stackPointer))
            newInstructions.add(MipsInstruction("jr", Register(config.returnAddressRegister)))

            val newFunction = MipsFunction(functionName, newInstructions, stackVariables, registers, environmentVariables)
            programBuilder.addFunction(newFunction)

            return newFunction
        }

        fun getClosureCreationCode(config: MipsConfiguration, function: MipsFunction, parentVariables: Int): List<MipsInstruction> = listOf(
            MipsInstruction("la", Register(config.argumentRegisters[0]), Label(function.label)),
            MipsInstruction("li", Register(config.argumentRegisters[1]), Value(function.environmentVariables * 4)),
            MipsInstruction("li", Register(config.argumentRegisters[2]), Value(parentVariables * 4)),
            MipsInstruction("jal", Label(PROCEDURE_CREATE_CLOSURE))
        )
    }

    private fun createRegisterInstructions(registers: Set<String>, instruction: String, storageRegister: String): List<MipsInstruction> = registers.mapIndexed { index, register ->
        MipsInstruction(instruction, Register(register), IndirectRegister(storageRegister, index * -4))
    }

    private fun addConstruct(construct: TacStructure) {
//        builder.add(MipsInstruction(""))
//        builder.add(MipsInstruction("# ${construct::class.simpleName} ($construct)"))
        when(construct) {
            is ConstructPushArgument -> {
                val argumentValueRegister = Register(config.argumentRegisters[0])
                val stackPointer = config.stackPointer
                copyToRegister(argumentValueRegister, construct.variable)

                builder.add(MipsInstruction("sw", argumentValueRegister, IndirectRegister(stackPointer, 0)))
                builder.add(MipsInstruction("addi", Register(stackPointer), Register(stackPointer), Value(-4)))
            }
            is ConstructPopArgument -> {
                //No need to clear the argument from the stack
                val stackPointer = Register(config.stackPointer)
                builder.add(MipsInstruction("addi", stackPointer, stackPointer, Value(4)))
            }
            is ConstructFunctionCall -> {
                val closureRegister = Register(config.argumentRegisters[0])
                copyToRegister(closureRegister, construct.function)

                val codeAddressRegister = Register(config.argumentRegisters[1])
                builder.add(MipsInstruction("lw", codeAddressRegister, IndirectRegister(closureRegister.name, 0)))
                builder.add(MipsInstruction("jalr", codeAddressRegister))
            }
            is ConstructSetVariable -> {
                val variable = construct.variable
                when(variable) {
                    is RegisterVariable -> {
                        if(registerMapping.isPhysicalRegister(variable)) {
                            copyToRegister(createTemporaryRegister(registerMapping.getPhysicalRegister(variable)), construct.value)
                        } else {
                            val register = allocateRegister(construct.value, config.temporaryRegisters.last())
                            builder.add(MipsInstruction("sw", register, createStackVariableRegister(registerMapping.getStackRegister(variable))))
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
                            Register(memLocationRegister),
                            IndirectRegister(config.currentEnvironmentRegister, index * 4)
                        ))
                        builder.add(MipsInstruction(
                            "sw", valueRegister,
                            IndirectRegister(memLocationRegister, 0)
                        ))
                    }
                    is ReturnedValue -> {
                        copyToRegister(Register(config.resultRegister), construct.value)
                    }
                }
            }
            is ConstructBranchEqualZero -> {
                val register = allocateRegister(construct.conditionVariable, config.temporaryRegisters.last())
                builder.add(MipsInstruction("beq", register, Register(config.zeroRegister), Label(construct.branchLabel)))
            }
            is ConstructGoto -> {
                builder.add(MipsInstruction("j", Label(construct.label)))
            }
            is ConstructLabel -> {
                builder.add(MipsInstruction("${construct.label}:"))
            }
            is ConstructReturn -> {
                builder.add(MipsInstruction("j", Label(endFuncLabel)))
            }
            else -> throw IllegalStateException(construct::class.simpleName)
        }
    }

    private fun getVariableRegister(name: String): IndirectRegister {
        return createStackVariableRegister(getFrameOffset(name))
    }

    private fun getFrameOffset(name: String): Int {
        val index = this.function.variables.keys.indexOf(name)
        if(index < 0) {
            throw IllegalArgumentException("Variable $name not found")
        }
        return index
    }

    private fun copyToRegister(register: MipsArgument, expression: TacExpression) {
        val finalInstruction = expression.getCopyToRegisterInstruction()
        builder.add(MipsInstruction(finalInstruction.instruction, register, *finalInstruction.args.toTypedArray()))
    }

    private fun allocateRegister(expression: TacExpression, defaultRegister: String): Register {
        if(expression is RegisterVariable && registerMapping.isPhysicalRegister(expression)) {
            return createTemporaryRegister(registerMapping.getPhysicalRegister(expression))
        }
        if(expression is EnvironmentVariable) {
            val index = function.environment.indexOf(expression)
            val tempRegister = Register(defaultRegister)
            builder.add(MipsInstruction("lw", tempRegister, IndirectRegister(config.currentEnvironmentRegister, index * 4)))
            builder.add(MipsInstruction("lw", tempRegister, IndirectRegister(tempRegister.name, 0)))
            return Register(defaultRegister)
        }
        copyToRegister(Register(defaultRegister), expression)
        return Register(defaultRegister)
    }

    private fun TacExpression.getCopyToRegisterInstruction() = when(this) {
        is StringVariable -> MipsInstruction("la", Label("STRING${stringIndex}"))
        is IntValue -> {
            if(this.value > Int.MAX_VALUE || this.value < Int.MIN_VALUE) {
                throw IllegalArgumentException("Value of ${this.value} is larger than the maximum mips integer size")
            }
            MipsInstruction("li", Value(value.toInt()))
        }
        is RegisterVariable -> {
            if(registerMapping.isPhysicalRegister(this)) {
                MipsInstruction("move", Register(config.temporaryRegisters[registerMapping.getPhysicalRegister(this)]))
            } else {
                MipsInstruction("lw", createStackVariableRegister(registerMapping.getStackRegister(this)))
            }
        }
        is ReturnedValue -> MipsInstruction("move", Register(config.resultRegister))
        is StackVariable -> MipsInstruction("lw", getVariableRegister(name))
        is EnvironmentVariable -> {
            val index = function.environment.indexOf(this)
            val tempRegister = Register(config.temporaryRegisters.last())
            builder.add(MipsInstruction("lw", tempRegister, IndirectRegister(config.currentEnvironmentRegister, index * 4)))
            MipsInstruction("lw", IndirectRegister(tempRegister.name, 0))
        }
        is ConstructNegation -> {
            copyToRegister(Register(config.argumentRegisters[0]), toNegate)
            MipsInstruction("neg", Register(config.argumentRegisters[0]))
        }
        is ParamReference -> MipsInstruction("lw", createParameterRegister(this.index))
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
            MipsInstruction("sub", Register(config.zeroRegister), register)
        }
        is TacFunction -> {
            val newClosureMips = toMips("${this@FunctionToMips.functionName}_${this.codeName}", this, config, programBuilder)
            for (instruction in getClosureCreationCode(config, newClosureMips, this@FunctionToMips.function.environment.newVariables.size)) {
                builder.add(instruction)
            }
            MipsInstruction("move", Register(config.resultRegister))
        }
        is TacInbuiltFunction -> {
            val emulatedFunction = MipsFunction(this.label, emptyList(), 0, 0, 0)
            for (instruction in getClosureCreationCode(config, emulatedFunction, this@FunctionToMips.function.environment.newVariables.size)) {
                builder.add(instruction)
            }
            MipsInstruction("move", Register(config.resultRegister))
        }
        else -> throw IllegalStateException(this::class.simpleName)
    }

    private fun createStackVariableRegister(index: Int): IndirectRegister {
        return IndirectRegister(config.framePointer, (index + 1) * 4)
    }

    private fun createParameterRegister(index: Int): IndirectRegister {
        val pos = this.frameSize + (this.function.parameters - index)
        return IndirectRegister(config.framePointer, pos * 4)
    }

    private fun createTemporaryRegister(index: Int): Register {
        return Register(this.config.temporaryRegisters[index])
    }
}