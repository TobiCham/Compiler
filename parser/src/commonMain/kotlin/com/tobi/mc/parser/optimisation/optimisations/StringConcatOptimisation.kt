package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.StringConcat
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.optimisation.InstanceOptimisation
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.util.DescriptionMeta

internal object StringConcatOptimisation : InstanceOptimisation<StringConcat>(StringConcat::class) {
    override val description: DescriptionMeta = SimpleDescription("String concatenation", """
        Optimises "xxx" ++ "yyy" to "xxxyyy"
    """.trimIndent())

    override fun StringConcat.optimise(replace: (Computable) -> Boolean): Boolean {
        if(this.str1 is DataTypeString && this.str2 is DataTypeString) {
            return replace(DataTypeString((this.str1 as DataTypeString).value + (this.str2 as DataTypeString).value))
        }
        return false
    }
}