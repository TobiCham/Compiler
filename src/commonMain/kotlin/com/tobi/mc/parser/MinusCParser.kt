package com.tobi.mc.parser

import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.ast.SimpleReader
import com.tobi.mc.parser.ast.SimpleStringReader
import com.tobi.mc.parser.ast.lexer.Lexer
import com.tobi.mc.parser.ast.lexer.LexerNode
import com.tobi.mc.parser.ast.lexer.LexerNodeType
import com.tobi.mc.parser.ast.parser.Parser
import com.tobi.mc.parser.ast.parser.runtime.Scanner
import com.tobi.mc.parser.ast.parser.runtime.Symbol
import com.tobi.mc.parser.ast.parser.runtime.SymbolFactory
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.syntax.SyntaxValidator
import com.tobi.mc.parser.types.TypeDetector

class MinusCParser(val config: ParserConfiguration = ParserConfiguration()) {

    fun parse(program: String): Program {
        return parse(SimpleStringReader(program))
    }

    fun parse(reader: SimpleReader): Program {
        val program = parseToAST(reader)
        processAST(program)
        return program
    }

    private fun processAST(program: Program): Program {
        if(config.resolveIndices) {
            ContextIndexResolver.calculateVariableContexts(program)
        }
        SyntaxValidator(config.syntaxRules).validateSyntax(program)

        if(config.doTypeInference) {
            TypeDetector.inferAndValidateTypes(program)
        }

        val optimisedProgram = optimiseProgram(program)
        return optimisedProgram
    }

    private fun optimiseProgram(program: Program): Program {
        val result = ProgramOptimiser(config.optimisations).optimise(program)
        return when(result) {
            is Program -> result
            is ExpressionSequence -> Program(result, program.context, result.sourceRange)
            else -> Program(ExpressionSequence(result), program.context, result.sourceRange)
        }
    }

    fun parseToAST(reader: SimpleReader): Program {
        val lexer = Lexer(reader)
        val x = this::convertIdToName
        val parser = Parser(object : Scanner {
            override fun next_token(): Symbol? {
                val node = lexer.yylex() ?: return null
                return convertNodeToToken(node)
            }
        }, SymbolFactory(), config.defaultContext, x)

        val symbol = parser.parse()
        return symbol.value as Program
    }

    private fun convertNodeToToken(node: LexerNode): Symbol {
        return Symbol(node.type.representation, node.type.parserId, node.startPosition, node.endPosition, node.value)
    }

    private fun convertIdToName(id: Int): String = LexerNodeType.values().find {
        it.parserId == id
    }?.representation ?: "unknown"
}