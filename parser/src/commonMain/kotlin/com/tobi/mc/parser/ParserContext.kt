package com.tobi.mc.parser

import com.tobi.mc.computable.Program

interface ParserContext {

    val validator: SyntaxValidator

    val typeDetection: TypeDetection

    val optimiser: SyntaxOptimiser

    fun getAllOperations(): Set<ParserOperation>

    fun getParserOperationFlow(): List<ParserOperation>

    /**
     * @throws [ParseException]
     */
    fun parseFromString(program: String): Program

    /**
     * Executes all operations in [getParserOperationFlow] to produce a resulting valid program which can be executed
     * @return A new program
     */
    fun processProgram(program: Program)

    companion object {

        fun createContext(): ParserContext = ParserContextImpl()
    }
}