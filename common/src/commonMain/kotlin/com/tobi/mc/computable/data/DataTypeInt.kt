package com.tobi.mc.computable.data

import com.tobi.mc.computable.Data

data class DataTypeInt(val value: Long) : Data() {

    override val type: DataType = DataType.INT

    override fun toString(): String = value.toString()

    override fun toScriptString(): String = value.toString()
}