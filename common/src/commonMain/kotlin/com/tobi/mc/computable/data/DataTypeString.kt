package com.tobi.mc.computable.data

import com.tobi.mc.computable.Data

class DataTypeString(val value: String) : Data() {

    override val type: DataType = DataType.STRING

    override fun toString(): String = "\"${value.escape()}\""

    override fun toScriptString(): String = value

    private fun String.escape(): String {
        var result = this
        result = result.replace("\n", "\\n")
        result = result.replace("\t", "\\t")
        result = result.replace("\"", "\\\"")

        return result
    }
}
