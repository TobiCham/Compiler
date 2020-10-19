package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.operation.Mod
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.isOne
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object ModOneOptimisation : InstanceOptimisation<Mod>(Mod::class) {

    override val description: DescriptionMeta = SimpleDescription("Mod by 1 identity", """
        Optimises expressions in the form of:
        - x % 1
        To 0
    """.trimIndent())

    override fun Mod.optimiseInstance(): Computable? = when {
        arg2.isOne() && (arg1 is DataTypeInt || arg1 is GetVariable)  -> DataTypeInt(0)
        else -> null
    }
}