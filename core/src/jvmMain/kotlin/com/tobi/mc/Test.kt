package com.tobi.mc

import com.tobi.mc.parser.syntax.types.*
import java.io.StringReader

fun main() {
    while(true) {
        println("Enter a function type")
        val t1 = readType(readLine()!!)
        println("Enter number of args:")
        val argCount = readLine()!!.toInt()

        if(argCount != 0) {
            println("Enter each arg type in order:")
        }
        val args = Array(argCount) {
            readType(readLine()!!)
        }
        try {
            println(TypeMerger.invokeFunction(t1, args.toList()))
        } catch (e: Exception) {
            System.err.println(e.message)
        }
    }
}

private fun readType(input: String): AnalysisType  {
    val lexer = Lexer(StringReader(input))
    return lexer.readType()
}

private fun Lexer.readType(): AnalysisType {
    val token = this.readToken()
    return handleReadType(token, this)
}

private fun handleReadType(token: TokenType, lexer: Lexer): AnalysisType = when(token) {
    TokenType.INT -> AnalysisIntType
    TokenType.STRING -> AnalysisStringType
    TokenType.VOID -> AnalysisVoidType
    TokenType.ANYTHING -> AnalysisAnythingType
    TokenType.UNKNOWN -> AnalysisUnknownType
    TokenType.START_FUNCTION -> lexer.readFunction()
    TokenType.START_INTERSECTION -> lexer.readIntersection()
    else -> throw IllegalStateException("Unexpected token ${token.name.toLowerCase()}")
}

private fun Lexer.readFunction(): AnalysisFunctionType {
    val returnType = this.readType()
    this.readToken(TokenType.FUNCTION)
    this.readToken(TokenType.START_PARAMS)

    var iteration = 0
    var unknown = false
    val paramList = ArrayList<AnalysisType>()

    while(true) {
        var token = this.readToken()
        if(iteration == 0 && token == TokenType.END_PARAMS) break
        if(token == TokenType.UNKNOWN_PARAMS) {
            if(iteration == 0) {
                unknown = true
                this.readToken(TokenType.END_PARAMS)
                break
            }
            throw IllegalStateException("Unexpected token ${token.name.toLowerCase()}")
        }
        val type = handleReadType(token, this)
        paramList.add(type)

        token = this.readToken()
        if(token == TokenType.END_PARAMS) break
        if(token == TokenType.PARAM_SEPARATOR) {
            iteration++
            continue
        }
        throw IllegalStateException("Unexpected token ${token.name.toLowerCase()}")
    }
    this.readToken(TokenType.END_FUNCTION)

    val params = if(unknown) AnalysisUnknownParams else AnalysisKnownParams(paramList)
    return AnalysisFunctionType(returnType, params)
}

private fun Lexer.readIntersection(): AnalysisIntersectionType {
    val types = LinkedHashSet<AnalysisType>()
    var iteration = 0
    while(true) {
        var token = this.readToken()
        if(token == TokenType.END_INTERSECTION && iteration == 0) break
        types.add(handleReadType(token, this))

        token = this.readToken()
        if(token == TokenType.END_INTERSECTION) break
        if(token == TokenType.OR) {
            iteration++
            continue
        }
        throw IllegalStateException("Expected token ${token.name.toLowerCase()}")
    }
    return AnalysisIntersectionType(types)
}

fun Lexer.readToken(expected: TokenType) {
    val token = this.readToken()
    if(token != expected) {
        throw IllegalStateException("Expected ${expected.name.toLowerCase()}, got ${token.name.toLowerCase()}")
    }
}
fun Lexer.readToken(): TokenType = this.yylex() ?: throw IllegalStateException("Unexpected end of file")