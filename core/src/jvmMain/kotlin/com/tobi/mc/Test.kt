package com.tobi.mc

import com.tobi.mc.parser.syntax.types.*
import java.io.StringReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

fun main() {
    while(true) {
        println("""
            Enter action:
            1. Invoke function
            2. Can be assigned?
            3. Merge types
        """.trimIndent())

        when(readLine()?.toIntOrNull()) {
            1 -> {
                println("Enter function, then all args followed by an empty line:")
                val func = readType(readLine()!!)
                val args = LinkedList<ExpandedType>()
                while(true) {
                    val line = readLine()
                    if(line.isNullOrBlank()) break
                    args.add(readType(line))
                }
                try {
                    println(TypeMerger.invokeFunction(func, args))
                } catch (e: Exception) {
                    System.err.println(e.message)
                }
            }
            2 -> {
                println("Enter two types:")
                val type1 = readType(readLine()!!)
                val type2 = readType(readLine()!!)
                println(TypeMerger.canBeAssignedTo(type1, type2).toString())
            }
            3 -> {
                println("Enter two types:")
                val type1 = readType(readLine()!!)
                val type2 = readType(readLine()!!)
                println(TypeMerger.mergeTypes(type1, type2).toString())
            }
            else -> System.err.println("Invalid option")
        }
    }
}

private fun readType(input: String): ExpandedType  {
    val lexer = Lexer(StringReader(input))
    return lexer.readType()
}

private fun Lexer.readType(): ExpandedType {
    val token = this.readToken()
    return handleReadType(token, this)
}

private fun handleReadType(token: TokenType, lexer: Lexer): ExpandedType = when(token) {
    TokenType.INT -> IntType
    TokenType.STRING -> StringType
    TokenType.VOID -> VoidType
    TokenType.ANYTHING -> UnknownType
    TokenType.START_FUNCTION -> lexer.readFunction()
    TokenType.START_INTERSECTION -> lexer.readIntersection()
    else -> throw IllegalStateException("Unexpected token ${token.name.toLowerCase()}")
}

private fun Lexer.readFunction(): FunctionType {
    val returnType = this.readType()
    this.readToken(TokenType.START_PARAMS)

    var iteration = 0
    var unknown = false
    val paramList = ArrayList<ExpandedType>()

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

    val params = if(unknown) UnknownParameters else KnownParameters(paramList)
    return FunctionType(returnType, params)
}

private fun Lexer.readIntersection(): IntersectionType {
    val types = LinkedHashSet<ExpandedType>()
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
    return IntersectionType(types)
}

fun Lexer.readToken(expected: TokenType) {
    val token = this.readToken()
    if(token != expected) {
        throw IllegalStateException("Expected ${expected.name.toLowerCase()}, got ${token.name.toLowerCase()}")
    }
}
fun Lexer.readToken(): TokenType = this.yylex() ?: throw IllegalStateException("Unexpected end of file")