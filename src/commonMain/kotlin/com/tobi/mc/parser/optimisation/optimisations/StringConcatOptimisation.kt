package com.tobi.mc.parser.optimisation.optimisations

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.optimisation.ASTInstanceOptimisation
import com.tobi.mc.util.DescriptionMeta
import com.tobi.mc.util.SimpleDescription

object StringConcatOptimisation : ASTInstanceOptimisation<StringConcat>(StringConcat::class) {

    override val description: DescriptionMeta = SimpleDescription("String concatenation", """
        Optimises "xxx" ++ "yyy" to "xxxyyy"
    """.trimIndent())

    override fun StringConcat.optimiseInstance(): OptimisationResult<DataTypeString> {
        if(this.str1 is DataTypeString && this.str2 is DataTypeString) {
            return newValue(DataTypeString((this.str1 as DataTypeString).value + (this.str2 as DataTypeString).value))
        }
        return noOptimisation()
    }
}