package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacFunction
import com.tobi.mc.intermediate.construct.code.RegisterVariable
import com.tobi.mc.intermediate.util.getComponents
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object OptimisationRegisters : TacInstanceOptimisation<TacFunction>(TacFunction::class) {

    override val description: DescriptionMeta = SimpleDescription("Registers", """
        Allows reuse of registers as soon as possible
    """.trimIndent())

    override fun TacFunction.optimise(): Boolean {
        listRegisterExtents(this).toList().sortedBy { it.register }.forEach(::println)
        return false
    }

    private fun listRegisterExtents(function: TacFunction): Set<RegisterExtent> {
        fun addRegisters(line: Int, structure: TacStructure, registers: MutableMap<Int, MutableSet<Int>>) {
            if(structure is RegisterVariable) {
                registers.getOrPut(structure.register, ::LinkedHashSet).add(line)
            }
            for (component in structure.getComponents()) {
                addRegisters(line, component, registers)
            }
        }
        val registers = LinkedHashMap<Int, MutableSet<Int>>()
        for((line, construct) in function.code.withIndex()) {
            addRegisters(line, construct, registers)
        }
        return LinkedHashSet(registers.entries.map { (register, lines) ->
            RegisterExtent.fromList(register, lines)
        })
    }

    private data class RegisterExtent(val register: Int, val startLine: Int, val endLine: Int) {

        companion object {
            fun fromList(register: Int, lines: Collection<Int>): RegisterExtent {
                if(lines.isEmpty()) {
                    throw IllegalArgumentException("No lines")
                }
                return RegisterExtent(register, lines.minOrNull()!!, lines.maxOrNull()!!)
            }
        }
    }
}