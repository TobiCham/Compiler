package com.tobi.mc.computable.data

import com.tobi.mc.computable.Data
import com.tobi.mc.util.escapeForPrinting

class DataTypeString(val value: String) : Data() {

    override val type: DataType = DataType.STRING

    override fun toString(): String = "\"${value.escapeForPrinting()}\""

    override fun toScriptString(): String = value
}
