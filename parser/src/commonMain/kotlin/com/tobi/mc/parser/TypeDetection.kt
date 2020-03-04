package com.tobi.mc.parser

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DefaultContext

interface TypeDetection : ParserOperation {

    fun <T : Computable> inferAndValidateTypes(computable: T, defaultContext: DefaultContext)
}