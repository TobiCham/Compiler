package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.util.DescriptionMeta

interface SyntaxRule {

    val description: DescriptionMeta

    fun validate(computable: Computable)
}