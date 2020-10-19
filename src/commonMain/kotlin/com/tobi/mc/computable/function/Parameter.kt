package com.tobi.mc.computable.function

import com.tobi.mc.SourceObject
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.data.DataType

data class Parameter(
    val type: DataType,
    val name: String,
    override var sourceRange: SourceRange? = null
) : SourceObject