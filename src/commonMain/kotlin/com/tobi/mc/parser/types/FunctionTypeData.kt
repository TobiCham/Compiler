package com.tobi.mc.parser.types

import com.tobi.mc.ParseException
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.type.ExpandedType
import com.tobi.mc.type.VoidType

class FunctionTypeData(val function: FunctionDeclaration, declaredReturnType: ExpandedType?) {

    private var returnType: ExpandedType? = declaredReturnType

    fun getReturnType(): ExpandedType {
        return returnType ?: VoidType
    }

    fun addReturnType(returnStatement: ReturnStatement, type: ExpandedType) {
        if(returnType == null) returnType = type
        else {
            if(!TypeMerger.canBeAssignedTo(returnType!!, type)) {
                if(function.returnType == null) {
                    throw ParseException("Conflicting calculated return types '$returnType' and '$type'", returnStatement)
                } else {
                    throw ParseException("Cannot return '${type}', expected '$returnType'", returnStatement)
                }
            }
            returnType = TypeMerger.mergeTypes(returnType!!, type)
        }
    }
}