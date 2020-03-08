package com.tobi.mc.type

import com.tobi.mc.computable.data.DataType

sealed class ExpandedType
interface CompleteType {
    val type: DataType
}
interface ComplexType

object IntType : ExpandedType(),
    CompleteType {
    override val type: DataType = DataType.INT
    override fun toString(): String = DataType.INT.toString()
}
object StringType : ExpandedType(),
    CompleteType {
    override val type: DataType = DataType.STRING
    override fun toString(): String = DataType.STRING.toString()
}
object VoidType : ExpandedType(),
    CompleteType {
    override val type: DataType = DataType.VOID
    override fun toString(): String = DataType.VOID.toString()
}
object UnknownType : ExpandedType() {
    override fun toString(): String = "?"
}

data class FunctionType(val returnType: ExpandedType, val parameters: AnalysisParamList) : ExpandedType(), ComplexType, CompleteType {

    override val type: DataType = DataType.FUNCTION

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