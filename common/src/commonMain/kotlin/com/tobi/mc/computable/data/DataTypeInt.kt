package com.tobi.mc.computable.data

data class DataTypeInt(val value: Long) : Data() {

    override val type: DataType = DataType.INT

    override fun toString(): String = value.toString()
}