package com.tobi.mc.type

import com.tobi.mc.computable.data.DataType

sealed class ExpandedType
sealed class CompleteType(val type: DataType) : ExpandedType() {
    override fun toString(): String = type.toString()
}
interface ComplexType

object IntType : CompleteType(DataType.INT)
object StringType : CompleteType(DataType.STRING)
object VoidType : CompleteType(DataType.VOID)

object UnknownType : ExpandedType() {
    override fun toString(): String = "?"
}

data class FunctionType(val returnType: ExpandedType, val parameters: AnalysisParamList) : ComplexType, CompleteType(DataType.FUNCTION) {

    override fun toString(): String {
        val paramList = if(parameters is KnownParameters) {
            parameters.params.joinToString(", ")
        } else {
            "???"
        }
        return "[$returnType($paramList)]"
    }
}

data class IntersectionType(val possible: Set<ExpandedType>): ExpandedType(), ComplexType {
    override fun toString(): String = buildString {
        append('<')
        append(possible.joinToString(" | "))
        append('>')
    }
}

sealed class AnalysisParamList
data class KnownParameters(val params: List<ExpandedType>) : AnalysisParamList() {
    val size = params.size
}
object UnknownParameters : AnalysisParamList()