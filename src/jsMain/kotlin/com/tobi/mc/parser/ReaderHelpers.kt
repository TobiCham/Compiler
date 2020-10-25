package com.tobi.mc.parser

actual object ReaderHelpers {
    actual fun copyArray(
        source: CharArray,
        sourcePos: Int,
        destination: CharArray,
        destinationPos: Int,
        length: Int
    ) {
        val newSource = source.copyOf()
        for(i in 0 until length) {
            destination[destinationPos + i] = newSource[sourcePos + i]
        }
    }

    actual fun <T> copyArray(
        source: Array<T>,
        sourcePos: Int,
        destination: Array<T>,
        destinationPos: Int,
        length: Int
    ) {
        val newSource = source.copyOf()
        for(i in 0 until length) {
            destination[destinationPos + i] = newSource[sourcePos + i]
        }
    }

    actual fun charCount(value: Int): Int = 1

    actual fun codePointAt(source: CharArray, index: Int, limit: Int): Int = source[index].toInt()

    actual fun toCharArray(
        string: String,
        destination: CharArray,
        destinationOffset: Int,
        startIndex: Int,
        endIndex: Int
    ) {
        val len = endIndex - startIndex
        for(i in 0 until len) {
            destination[i + destinationOffset] = string[startIndex + i]
        }
    }

    actual fun <T> expandArray(source: Array<T?>, newSize: Int): Array<T?> {
        val extraSize = newSize - source.size
        val newArray = js("Array(extraSize)")
        return js("source.concat(newArray)")
    }
}