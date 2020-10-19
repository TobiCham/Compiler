package com.tobi.mc.parser.ast

interface SimpleReader {

    fun read(): Int

    fun read(buffer: CharArray, offset: Int, maxLength: Int): Int

    fun close()
}