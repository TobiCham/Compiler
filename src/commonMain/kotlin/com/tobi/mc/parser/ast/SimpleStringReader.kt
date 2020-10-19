package com.tobi.mc.parser.ast

import com.tobi.mc.parser.ReaderHelpers
import kotlin.math.min

class SimpleStringReader(private val input: String) : SimpleReader {

    private var closed = false
    private val length = input.length
    private var next = 0

    override fun read(): Int {
        return if (next >= length) -1 else input[next++].toInt()
    }

    override fun read(buffer: CharArray, offset: Int, maxLength: Int): Int {
        if(closed) {
            throw ReaderException("Reader closed")
        }

        return if (offset >= 0 && offset <= buffer.size && maxLength >= 0 && offset + maxLength <= buffer.size && offset + maxLength >= 0) {
            when {
                maxLength == 0 -> 0
                this.next >= this.length -> -1
                else -> {
                    val n: Int = min(this.length - this.next, maxLength)
                    ReaderHelpers.toCharArray(this.input, buffer, offset, this.next, this.next + n)
                    this.next += n
                    n
                }
            }
        } else {
            throw IndexOutOfBoundsException()
        }
    }

    override fun close() {
        closed = true
    }
}