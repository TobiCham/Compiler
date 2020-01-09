package com.tobi.mc.inbuilt

import com.tobi.mc.Context
import com.tobi.mc.Data
import com.tobi.mc.analysis.ExpandedType

abstract class InbuiltVariable(val name: String, val expandedType: ExpandedType) {

    abstract fun computeData(context: Context): Data
}