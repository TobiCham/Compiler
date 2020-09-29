package com.tobi.mc.type

import com.tobi.mc.computable.Computable

interface TypedComputable : Computable {

    val expandedType: ExpandedType
}