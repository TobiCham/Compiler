package com.tobi.mc.intermediate.optimisation

import com.tobi.mc.OptimisationResult
import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.code.RegisterVariable
import com.tobi.mc.intermediate.code.TacFunction
import com.tobi.mc.noOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object OptimisationRegisters : TacInstanceOptimisation<TacFunction>(TacFunction::class) {

    override val description: DescriptionMeta = SimpleDescription("Registers", """
        Allows reuse of registers as soon as possible
    """.trimIndent())

    override fun TacFunction.optimiseInstance(): OptimisationResult<TacNode> {
//        listRegisterExtents(this).toList().sortedBy { it.register }.forEach(::println)
        return noOptimisation()
    }

    private fun listRegisterExtents(function: TacFunction): Set<RegisterExtent> {
        fun addRegisters(line: Int, node: TacNode, registers: MutableMap<Int, MutableSet<Int>>) {
            if(node is RegisterVariable) {
                registers.getOrPut(node.register, ::LinkedHashSet).add(line)
            }
            for (node in node.getNodes()) {
                addRegisters(line, node, registers)
            }
        }
        val registers = LinkedHashMap<Int, MutableSet<Int>>()
        for((line, construct) in function.code.instructions.withIndex()) {
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