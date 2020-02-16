package com.tobi.mc.parser.syntax.types

import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.mc.parser.ParseException

class FunctionTypeData(val function: FunctionDeclaration, declaredReturnType: ExpandedType?) {

    var returnType: ExpandedType? = declaredReturnType

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