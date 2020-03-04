package com.tobi.mc.parser.ast

internal expect object ReaderHelpers {

    fun copyArray(source: CharArray, sourcePos: Int, destination: CharArray, destinationPos: Int, length: Int)

    fun <T> copyArray(source: Array<T>, sourcePos: Int, destination: Array<T>, destinationPos: Int, length: Int)

    fun charCount(value: Int): Int

    fun codePointAt(source: CharArray, index: Int, limit: Int): Int

    fun toCharArray(
        string: String,
        destination: CharArray,
        destinationOffset: Int,
        startIndex: Int,
        endIndex: Int
    )
}