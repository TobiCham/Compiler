package com.tobi.mc.parser.syntax.rules

import com.tobi.mc.ParseException
import com.tobi.mc.computable.operation.Divide
import com.tobi.mc.computable.operation.MathOperation
import com.tobi.mc.computable.operation.Mod
import com.tobi.mc.parser.syntax.InstanceSyntaxRule
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.isZero
import com.tobi.mc.util.DescriptionMeta

internal object RuleDivideByZero : InstanceSyntaxRule<MathOperation>(MathOperation::class) {

    override val description: DescriptionMeta = SimpleDescription("Divide by zero", """
        Prevents division or modulus by 0
    """.trimIndent())

    override fun MathOperation.validate() {
        if((this is Divide || this is Mod) && this.arg2.isZero()) {
            throw ParseException("Invalid division by zero - will always fail")
        }
    }
}