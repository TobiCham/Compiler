package com.tobi.mc.computable.data

import com.tobi.mc.computable.Data

object DataTypeVoid : Data() {

    override val type: DataType = DataType.VOID

    override fun toScriptString(): String = "void"

    override fun toString(): String = "void"
}