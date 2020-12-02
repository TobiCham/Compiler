package com.tobi.mc.mips

import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.RegisterVariable
import com.tobi.mc.intermediate.util.asDeepSequence

class RegisterMapping(private val function: TacFunction, private val availableRegisters: Int) {

    private val registerToPhysicalRegister = HashMap<Int, Int>()
    private val registerToStackVariable = HashMap<Int, Int>()

    /**
     * The number of excess registers which are instead stored on the stack
     */
    val excessRegisters: Int

    /**
     * The number of physical registers used
     */
    val physicalRegistersCount: Int

    val physicalRegisters: Set<Int>
        get() = LinkedHashSet(registerToPhysicalRegister.values)

    init {
        //Registers which are used more frequently should be mapped where possible to physical registers
        //whereas registers which are used infrequently may not be as important

        val registerCount = HashMap<Int, Int>()
        for(structure in function.asDeepSequence()) {
            if(structure is RegisterVariable) {
                registerCount[structure.register] = (registerCount[structure.register] ?: 0) + 1
            }
        }
        val sortedRegisters = registerCount.map { (register, count) ->
            Pair(register, count)
        }.sortedBy { it.second }
        for ((i, pair) in sortedRegisters.withIndex()) {
            val register = pair.first
            if(i < availableRegisters) {
                this.registerToPhysicalRegister[register] = i
            } else {
                this.registerToStackVariable[register] = i
            }
        }

        physicalRegistersCount = registerToPhysicalRegister.size
        excessRegisters = registerToStackVariable.size
    }

    fun isStackRegister(register: RegisterVariable): Boolean {
        return this.registerToStackVariable.containsKey(register.register)
    }

    fun isPhysicalRegister(register: RegisterVariable): Boolean {
        return this.registerToPhysicalRegister.containsKey(register.register)
    }

    fun getPhysicalRegister(register: RegisterVariable): Int {
        return this.registerToPhysicalRegister[register.register]
            ?: throw IllegalArgumentException("Register t${register.register} has not been allocated a physical register")
    }

    fun getStackRegister(register: RegisterVariable): Int {
        val id = this.registerToStackVariable[register.register]
            ?: throw IllegalArgumentException("Register ${register.register} has not been allocated a stack variable")

        return id + function.variables.size
    }
}