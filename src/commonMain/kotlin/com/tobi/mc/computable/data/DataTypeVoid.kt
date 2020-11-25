package com.tobi.mc.computable.data

import com.tobi.mc.SourceRange

data class DataTypeVoid(override var sourceRange: SourceRange? = null) : Data() {

    override val type: DataType = DataType.VOID

    override fun toString(): String = "void"
}