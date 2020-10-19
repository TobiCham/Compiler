package com.tobi.mc.computable.data

import com.tobi.mc.SourceRange

data class DataTypeInt(val value: Long, override var sourceRange: SourceRange? = null) : Data() {

    override val type: DataType = DataType.INT

    override fun toString(): String = value.toString()
}