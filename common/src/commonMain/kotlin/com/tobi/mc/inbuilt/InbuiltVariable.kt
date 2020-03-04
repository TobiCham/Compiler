package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.Data
import com.tobi.mc.type.ExpandedType

abstract class InbuiltVariable(val name: String, val expandedType: ExpandedType) {

    abstract fun computeData(context: Context): Data
}