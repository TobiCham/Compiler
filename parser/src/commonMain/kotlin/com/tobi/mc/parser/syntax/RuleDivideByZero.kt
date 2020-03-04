package com.tobi.mc.parser.syntax

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Divide
import com.tobi.mc.computable.MathOperation
import com.tobi.mc.computable.Mod
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isZero
import com.tobi.util.DescriptionMeta

internal object RuleDivideByZero : SimpleRule {

    override val description: DescriptionMeta = SimpleDescription("Divide by zero", """
        Prevents division or modulus by 0
    """.trimIndent())

    override fun validate(computable: Computable, state: Unit) {
        if((computable is Divide || computable is Mod) && (computable as MathOperation).arg2.isZero()) {
            throw ParseException("Invalid division by zero - will always fail")
        }
    }
}