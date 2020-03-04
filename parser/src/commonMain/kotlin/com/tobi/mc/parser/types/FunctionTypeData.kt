package com.tobi.mc.parser.types

import com.tobi.mc.ParseException
import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.type.ExpandedType
import com.tobi.mc.type.VoidType

internal class FunctionTypeData(val function: FunctionDeclaration, declaredReturnType: ExpandedType?) {

    private var returnType: ExpandedType? = declaredReturnType

    fun getReturnType(): ExpandedType {
        return returnType ?: VoidType
    }

    fun addReturnType(type: ExpandedType) {
        if(returnType == null) returnType = type
        else {
            if(!TypeMerger.canBeAssignedTo(returnType!!, type)) {
                if(function.returnType == null) {
                    throw ParseException("Conflicting return types for function ${function.name}: '$returnType' and '$type'")
                } else {
                    throw ParseException("Cannot return '$type' from ${function.name} ($returnType)")
                }
            }
            returnType = TypeMerger.mergeTypes(returnType!!, type)
        }
    }
}