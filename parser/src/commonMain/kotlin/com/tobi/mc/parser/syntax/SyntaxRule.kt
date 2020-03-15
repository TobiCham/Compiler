package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.util.DescriptionMeta

internal interface SyntaxRule<T : Computable> {

    val description: DescriptionMeta

    fun accepts(computable: Computable): Boolean

    fun T.validate()
}