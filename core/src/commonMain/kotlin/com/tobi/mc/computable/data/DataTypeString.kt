package com.tobi.mc.computable.data

import com.tobi.mc.Data

class DataTypeString(val value: String) : Data() {

    override val type: DataType = DataType.STRING

    override fun toString(): String = "\"$value\""

    override fun toScriptString(): String = value
}
