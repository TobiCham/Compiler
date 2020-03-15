package com.tobi.mc.parser

import com.tobi.mc.computable.Program
import com.tobi.mc.util.DescriptionMeta

interface ParserOperation {

    /**
     * Represents an operation during the initial compilation stage, which may or may not modify the syntax tree
     */
    fun processProgram(program: Program)

    val description: DescriptionMeta
}