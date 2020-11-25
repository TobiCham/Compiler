package com.tobi.mc.computable.data

import com.tobi.mc.SourceRange
import com.tobi.mc.util.escapeForPrinting

data class DataTypeString(val value: String, override var sourceRange: SourceRange? = null) : Data() {

    override val type: DataType = DataType.STRING

    override fun toString(): String = "\"${value.escapeForPrinting()}\""
}
