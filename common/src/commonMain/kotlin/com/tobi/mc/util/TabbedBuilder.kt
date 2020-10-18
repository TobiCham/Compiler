package com.tobi.mc.util

class TabbedBuilder(val tabSize: Int = 4) {

    private var indent = 0
    private val builder = StringBuilder()
    private var currentStr = StringBuilder()

    fun indent(): TabbedBuilder {
        indent += tabSize
        return this
    }

    fun outdent(): TabbedBuilder {
        indent -= tabSize
        check(indent >= 0) { "Cannot outdent any more" }
        return this
    }

    fun print(value: Any?) {
        currentStr.append(value)
    }

    fun println(value: Any?) {
        print(value)
        if(indent > 0) {
            val chars = CharArray(indent) { ' ' }
            builder.append(chars.concatToString())
        }
        builder.append(currentStr)
        builder.append('\n')
        currentStr.clear()
    }

    override fun toString(): String = builder.toString() + currentStr.toString()
}