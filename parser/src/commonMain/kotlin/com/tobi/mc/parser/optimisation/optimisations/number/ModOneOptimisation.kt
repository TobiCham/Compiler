package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.GetVariable
import com.tobi.mc.computable.Mod
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isOne
import com.tobi.util.DescriptionMeta

internal object ModOneOptimisation : Optimisation<Mod> {

    override val description: DescriptionMeta = SimpleDescription("Mod by 1 identity", """
        Optimises expressions in the form of:
        - x % 1
        To 0
    """.trimIndent())

    override fun accepts(computable: Computable): Boolean = computable is Mod

    override fun Mod.optimise(replace: (Computable) -> Boolean) = when {
        arg2.isOne() && (arg1 is DataTypeInt || arg1 is GetVariable)  -> replace(DataTypeInt(0))
        else -> false
    }
}