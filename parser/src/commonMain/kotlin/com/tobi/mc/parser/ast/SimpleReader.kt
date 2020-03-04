package com.tobi.mc.parser.ast

internal interface SimpleReader {

    fun read(buffer: CharArray, offset: Int, maxLength: Int): Int

    fun close()
}