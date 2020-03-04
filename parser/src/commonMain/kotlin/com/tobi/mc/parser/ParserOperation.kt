package com.tobi.mc.parser

import com.tobi.mc.computable.Program
import com.tobi.util.DescriptionMeta

interface ParserOperation {

    /**
     * Represents an operation during the initial compilation stage, which may or may not modify the syntax tree
     * @return The modified program - may be the same object as what was passed (potentially with modifications)
     */
    fun processProgram(program: Program): Program

    val description: DescriptionMeta
}