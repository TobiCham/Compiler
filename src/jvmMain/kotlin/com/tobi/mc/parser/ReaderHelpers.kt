package com.tobi.mc.parser

actual object ReaderHelpers {
    actual fun copyArray(
        source: CharArray,
        sourcePos: Int,
        destination: CharArray,
        destinationPos: Int,
        length: Int
    ) = System.arraycopy(source, sourcePos, destination, destinationPos, length)

    actual fun <T> copyArray(
        source: Array<T>,
        sourcePos: Int,
        destination: Array<T>,
        destinationPos: Int,
        length: Int
    ) = System.arraycopy(source, sourcePos, destination, destinationPos, length)

    actual fun charCount(value: Int): Int {
        return Character.charCount(value)
    }

    actual fun codePointAt(source: CharArray, index: Int, limit: Int): Int {
        return Character.codePointAt(source, index, limit)
    }

    actual fun toCharArray(
        string: String,
        destination: CharArray,
        destinationOffset: Int,
        startIndex: Int,
        endIndex: Int
    ) {
        string.toCharArray(destination, destinationOffset, startIndex, endIndex)
    }
}