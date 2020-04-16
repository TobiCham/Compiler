package com.tobi.mc.parser

import com.tobi.mc.computable.Program
import com.tobi.mc.parser.ast.SimpleStringReader
import com.tobi.mc.parser.ast.lexer.Lexer
import com.tobi.mc.parser.ast.lexer.LexerNode
import com.tobi.mc.parser.ast.lexer.LexerNodeType
import com.tobi.mc.parser.ast.parser.Parser
import com.tobi.mc.parser.ast.parser.runtime.FileLocation
import com.tobi.mc.parser.ast.parser.runtime.Scanner
import com.tobi.mc.parser.ast.parser.runtime.Symbol
import com.tobi.mc.parser.ast.parser.runtime.SymbolFactory
import com.tobi.mc.parser.optimisation.OptimisationImpl
import com.tobi.mc.parser.optimisation.OptimisationsList
import com.tobi.mc.parser.syntax.SyntaxRulesList
import com.tobi.mc.parser.syntax.SyntaxValidatorImpl
import com.tobi.mc.parser.types.TypeDetectionImpl

internal class ParserContextImpl : ParserContext {

    override val optimiser = OptimisationImpl(OptimisationsList.DEFAULT_OPTIMISATIONS)
    override val validator = SyntaxValidatorImpl(SyntaxRulesList.RULES)
    override val typeDetection = TypeDetectionImpl

    private val allOperations = setOf(optimiser, validator, typeDetection)
    private val orderedOperations = listOf(validator, optimiser, typeDetection, ContextIndexResolver)

    override fun getAllOperations(): Set<ParserOperation> = allOperations

    override fun getParserOperationFlow(): List<ParserOperation> = orderedOperations

    override fun processProgram(program: Program) {
        for (operation in getParserOperationFlow()) {
            operation.processProgram(program)
        }
    }

    override fun parseFromString(program: String): Program {
        try {
            return parseProgram(program)
        } catch (e: ParseException) {
            throw com.tobi.mc.ParseException(e.createErrorMessage(program))
        }
    }

    private fun parseProgram(program: String): Program {
        val lexer = Lexer(SimpleStringReader(program))
        val parser = Parser(object : Scanner {
            override fun next_token(): Symbol? {
                val node = lexer.yylex() ?: return null
                return convertNodeToToken(node)
            }
        }, SymbolFactory(), ::convertIdToName)

        val symbol = parser.parse()
        return symbol.value as Program
    }

    private fun convertNodeToToken(node: LexerNode): Symbol {
        val loc = FileLocation(node.line, node.column)
        val endExtra = if(node.value != null) node.value.toString() else node.type.representation
        val endLoc = FileLocation(node.line, node.column + endExtra.length)

        return Symbol(node.type.representation, node.type.parserId, loc, endLoc, node.value)
    }

    private fun convertIdToName(id: Int) = LexerNodeType.values().find { it.parserId == id }?.representation ?: "unknown"
}