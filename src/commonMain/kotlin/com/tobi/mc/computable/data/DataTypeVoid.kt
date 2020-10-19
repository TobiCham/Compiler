package com.tobi.mc.computable.data

import com.tobi.mc.SourceRange

class DataTypeVoid(override var sourceRange: SourceRange? = null) : Data() {

    override val type: DataType = DataType.VOID

    override fun toString(): String = "void"
}