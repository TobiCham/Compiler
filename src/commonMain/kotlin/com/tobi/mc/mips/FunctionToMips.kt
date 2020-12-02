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

    private val endFuncLabel = "${this.functionName}_end"
    private val registerMapping = RegisterMapping(function, config.temporaryRegisters.size - RESERVED_REGISTERS)
    private val frameSize =
            FUNCTION_DATA_SIZE +
            RESERVED_REGISTERS + registerMapping.physicalRegistersCount +
            this.function.variables.size + registerMapping.excessRegisters

    private val instructions = ArrayList<MipsInstruction>()
    private val registersToSave: Set<String>

    companion object {

        const val PROCEDURE_CREATE_CLOSURE = "createClosure"
        const val FUNCTION_DATA_SIZE = 2

        private const val RESERVED_REGISTERS = 2

        fun toMips(functionName: String, function: TacFunction, config: MipsConfiguration, programBuilder: MipsProgram.Builder): MipsFunction {
            val instance = FunctionToMips(functionName, function, config, programBuilder)

            instance.addEntryInstructions()
            function.code.forEach(instance::addConstruct)
            instance.addExitInstructions()

            val newFunction = instance.createFunction()
            programBuilder.addFunction(newFunction)

            return newFunction
        }

        fun getClosureCreationCode(config: MipsConfiguration, function: MipsFunction): List<MipsInstruction> = listOf(
            MipsInstruction("la", Register(config.argumentRegisters[0]), Label(function.label)),
            MipsInstruction("li", Register(config.argumentRegisters[1]), Value(function.environmentVariables * config.wordSize)),
            MipsInstruction("li", Register(config.argumentRegisters[2]), Value(function.parentEnvironments * config.wordSize)),
            MipsInstruction("jal", Label(PROCEDURE_CREATE_CLOSURE))
        )
    }

    init {
        this.registersToSave = LinkedHashSet<String>().apply {
            add(config.closureRegister)
            add(config.returnAddressRegister)

            addAll((0 until RESERVED_REGISTERS).map(::getReservedRegister))
            addAll(registerMapping.physicalRegisters.map(::getTemporaryRegister))
        }
    }

    private fun addEntryInstructions() {
        addRegisterInstructions("sw")
        instructions.addAll(
            MipsInstruction("addi", Register(config.stackPointer), Register(config.stackPointer), Value(-config.wordSize * (frameSize + this.function.parameters))),
            MipsInstruction("move", Register(config.closureRegister), Register(config.argumentRegisters[0]))
        )
    }

    private fun addExitInstructions() {
        instructions.addAll(
            MipsInstruction("${endFuncLabel}:"),
            MipsInstruction("addi", Register(config.stackPointer), Register(config.stackPointer), Value(config.wordSize * (frameSize + this.function.parameters)))
        )
        addRegisterInstructions("lw")
        instructions.add(MipsInstruction("jr", Register(config.returnAddressRegister)))
    }

    private fun addRegisterInstructions(instruction: String) {
        val instructions = registersToSave.mapIndexed { index, register ->
            MipsInstruction(instruction, Register(register), IndirectRegister(config.stackPointer, (index + this.function.parameters) * -config.wordSize))
        }
        this.instructions.addAll(instructions)
    }

    private fun addConstruct(construct: TacStructure) {
        when(construct) {
            is TacSetArgument -> {
                val stackPointer = config.stackPointer
                val argumentValueRegister = construct.variable.allocateRegister(getReservedRegister(0))

                instructions.add(MipsInstruction("sw", argumentValueRegister, IndirectRegister(stackPointer, construct.index * -config.wordSize)))
            }
            is TacFunctionCall -> {
                val closureRegister = construct.function.allocateRegister(config.argumentRegisters[0])
                val codeAddressRegister = Register(getTemporaryRegister(0))
                instructions.add(MipsInstruction("lw", codeAddressRegister, IndirectRegister(closureRegister.name, 0)))
                instructions.add(MipsInstruction("jalr", codeAddressRegister))
            }
            is TacSetVariable -> construct.createSetInstructions()
            is TacBranchEqualZero -> {
                val register = construct.conditionVariable.allocateRegister(getReservedRegister(0))
                instructions.add(MipsInstruction("beq", register, Register(config.zeroRegister), Label(construct.branchTo)))
            }
            is TacGoto -> {
                instructions.add(MipsInstruction("j", Label(construct.label)))
            }
            is TacLabel -> {
                instructions.add(MipsInstruction("${construct.label}:"))
            }
            is TacReturn -> {
                instructions.add(MipsInstruction("j", Label(endFuncLabel)))
            }
            else -> throw IllegalStateException(construct::class.simpleName)
        }
    }

    private fun TacSetVariable.createSetInstructions() {
        when(variable) {
            is RegisterVariable -> {
                if(registerMapping.isPhysicalRegister(variable)) {
                    value.copyToRegister(createTemporaryRegister(registerMapping.getPhysicalRegister(variable)))
                } else {
                    val register = value.allocateRegister(getReservedRegister(0))
                    instructions.add(MipsInstruction("sw", register, createStackVariableRegister(registerMapping.getStackRegister(variable))))
                }
            }
            is StackVariable -> {
                val register = value.allocateRegister(getReservedRegister(0))
                instructions.add(MipsInstruction("sw", register, getVariableRegister(variable.name)))
            }
            is EnvironmentVariable -> {
                val valueRegister = value.allocateRegister(getReservedRegister(0))
                val memLocationRegister = getReservedRegister(1)
                storeEnvironmentVariableArrayPointer(variable, memLocationRegister)
                val offset = getEnvironmentVariableOffset(variable)

                instructions.add(MipsInstruction(
                    "sw", valueRegister,
                    IndirectRegister(memLocationRegister, offset)
                ))
            }
            is ReturnedValue -> {
                value.copyToRegister(Register(config.resultRegister))
            }
        }
    }

    private fun getVariableRegister(name: String): IndirectRegister {
        return createStackVariableRegister(getFrameOffset(name))
    }

    private fun getFrameOffset(name: String): Int {
        val index = this.function.variables.indexOf(name)
        if(index < 0) {
            throw IllegalArgumentException("Variable $name not found")
        }
        return index
    }

    private fun TacExpression.copyToRegister(register: Register) {
        when(this) {
            is StringVariable -> {
                instructions.add(MipsInstruction("la", register, Label("STRING${stringIndex}")))
            }
            is IntValue -> {
                if(this.value > Int.MAX_VALUE || this.value < Int.MIN_VALUE) {
                    throw IllegalArgumentException("Value of ${this.value} is larger than the maximum mips integer size")
                }
                instructions.add(MipsInstruction("li", register, Value(value.toInt())))
            }
            is RegisterVariable -> {
                if(registerMapping.isPhysicalRegister(this)) {
                    val currentRegister = getTemporaryRegister(registerMapping.getPhysicalRegister(this))
                    if(currentRegister != register.name) {
                        instructions.add(MipsInstruction("move", register, Register(currentRegister)))
                    }
                } else {
                    instructions.add(MipsInstruction("lw", register, createStackVariableRegister(registerMapping.getStackRegister(this))))
                }
            }
            is ReturnedValue -> {
                if(register.name != config.resultRegister) {
                    instructions.add(MipsInstruction("move", register, Register(config.resultRegister)))
                }
            }
            is StackVariable -> {
                instructions.add(MipsInstruction("lw", register, getVariableRegister(name)))
            }
            is EnvironmentVariable -> {
                storeEnvironmentVariableArrayPointer(this, register.name)
                val offset = getEnvironmentVariableOffset(this)

                instructions.add(MipsInstruction("lw", register, IndirectRegister(register.name, offset)))
            }
            is TacNegation -> {
                toNegate.copyToRegister(register)
                instructions.add(MipsInstruction("neg", register, register))
            }
            is ParamReference -> {
                instructions.add(MipsInstruction("lw", register, getParameterRegister(this.index)))
            }
            is TacMathOperation -> {
                val r2 = Register(getReservedRegister(1))
                arg1.copyToRegister(register)
                arg2.copyToRegister(r2)

                instructions.add(when(type) {
                    TacMathOperation.MathType.ADD -> MipsInstruction("add", register, register, r2)
                    TacMathOperation.MathType.SUBTRACT -> MipsInstruction("sub", register, register, r2)
                    TacMathOperation.MathType.MULTIPLY -> MipsInstruction("mul", register, register, r2)
                    TacMathOperation.MathType.DIVIDE -> MipsInstruction("div", register, register, r2)
                    TacMathOperation.MathType.MOD -> MipsInstruction("rem", register, register, r2)
                    TacMathOperation.MathType.EQUALS -> MipsInstruction("seq", register, register, r2)
                    TacMathOperation.MathType.NOT_EQUALS -> {
                        instructions.add(MipsInstruction("seq", register, register, r2))
                        MipsInstruction("neg", register, register)
                    }
                    TacMathOperation.MathType.LESS_THAN -> MipsInstruction("slt", register, register, r2)
                    TacMathOperation.MathType.LESS_THAN_OR_EQUAL -> MipsInstruction("sle", register, register, r2)
                    TacMathOperation.MathType.GREATER_THAN -> MipsInstruction("sgt", register, register, r2)
                    TacMathOperation.MathType.GREATER_THAN_OR_EQUAL -> MipsInstruction("sge", register, register, r2)
                })
            }
            is TacUnaryMinus -> {
                this.variable.copyToRegister(register)
                instructions.add(MipsInstruction("sub", register, Register(config.zeroRegister), register))
            }
            is TacFunction -> {
                val newClosureMips = toMips("${this@FunctionToMips.functionName}_${this.codeName}", this, config, programBuilder)
                instructions.addAll(getClosureCreationCode(config, newClosureMips))

                if(register.name != config.resultRegister) {
                    instructions.add(MipsInstruction("move", register, Register(config.resultRegister)))
                }
            }
            is TacInbuiltFunction -> {
                val emulatedFunction = MipsFunction(this.label, emptyList(), 0, 0)
                for (instruction in getClosureCreationCode(config, emulatedFunction)) {
                    instructions.add(instruction)
                }
                if(register.name != config.resultRegister) {
                    instructions.add(MipsInstruction("move", register, Register(config.resultRegister)))
                }
            }
            else -> throw IllegalStateException(this::class.simpleName)
        }
    }

    private fun TacExpression.allocateRegister(defaultRegister: String): Register {
        if(this is RegisterVariable && registerMapping.isPhysicalRegister(this)) {
            return createTemporaryRegister(registerMapping.getPhysicalRegister(this))
        }
        val register = Register(defaultRegister)
        this.copyToRegister(register)
        return register
    }

    private fun getTemporaryRegister(index: Int): String {
        if(index < 0 || index >= config.temporaryRegisters.size - RESERVED_REGISTERS) {
            throw IllegalArgumentException("Invalid temporary register $index")
        }
        return config.temporaryRegisters[index]
    }

    private fun getReservedRegister(index: Int): String {
        if(index < 0 || index >= RESERVED_REGISTERS) {
            throw IllegalArgumentException("Invalid reserved register $index")
        }
        return config.temporaryRegisters[config.temporaryRegisters.size - index - 1]
    }

    private fun storeEnvironmentVariableArrayPointer(variable: EnvironmentVariable, storageRegister: String) {
        val arrayOffset = (variable.index + 1) * config.wordSize //+1 = start of the array (index=0 for the label)
        instructions.add(MipsInstruction("lw", Register(storageRegister), IndirectRegister(config.closureRegister, arrayOffset)))
    }

    private fun getEnvironmentVariableOffset(variable: EnvironmentVariable): Int {
        val index = function.environment.variables[variable.index].indexOf(variable.name)
        if(index < 0) {
            throw IllegalArgumentException("Unknown environment variable $variable")
        }
        return index * config.wordSize
    }

    private fun createStackVariableRegister(index: Int): IndirectRegister {
        return IndirectRegister(config.stackPointer, (index + 1) * config.wordSize)
    }

    private fun getParameterRegister(index: Int): IndirectRegister {
        val pos = this.frameSize + (this.function.parameters - index)
        return IndirectRegister(config.stackPointer, pos * config.wordSize)
    }

    private fun createTemporaryRegister(index: Int): Register {
        return Register(getTemporaryRegister(index))
    }

    private fun createFunction(): MipsFunction {
        val environmentVariables = function.environment.newVariables.size
        val parentEnvironments = function.environment.variables.size - 1

        return MipsFunction(functionName, instructions, environmentVariables, parentEnvironments)
    }
}