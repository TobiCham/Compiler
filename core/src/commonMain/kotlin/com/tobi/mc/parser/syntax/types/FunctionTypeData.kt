package com.tobi.mc.parser.syntax.types

import com.tobi.mc.analysis.ExpandedType
import com.tobi.mc.analysis.FunctionType
import com.tobi.mc.computable.FunctionDeclaration

class FunctionTypeData(val function: FunctionDeclaration) {

    val returnTypes: Set<ExpandedType?> = LinkedHashSet()

    fun addReturnType(type: ExpandedType) = apply {
        (returnTypes as MutableSet).add(type)
    }

    fun findCommonReturnType(): ExpandedType? = findCommonReturnType(this.returnTypes)

    @Suppress("UNCHECKED_CAST")
    private fun findCommonReturnType(types: Set<ExpandedType?>): ExpandedType? {
        if(types.isEmpty()) return null
        if(types.size == 1) return types.first()
        if(types.contains(null)) return null
        types as Set<ExpandedType>

        val typeMap = types.groupBy { it.type }.mapValues { HashSet(it.value) }
        if(typeMap.size != 1) return null

        val first = types.first()
        if(first !is FunctionType) return first

        val subTypes = HashSet(types.map { (it as FunctionType).returnType })
        return FunctionType(findCommonReturnType(subTypes))
    }
}