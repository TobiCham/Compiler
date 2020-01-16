package com.tobi.mc.parser.syntax.types

import com.tobi.mc.computable.data.DataType

sealed class AnalysisType
interface CompleteType {
    val type: DataType
}

object AnalysisIntType : AnalysisType(), CompleteType {
    override val type: DataType = DataType.INT
    override fun toString(): String = DataType.INT.toString()
}
object AnalysisStringType : AnalysisType(), CompleteType {
    override val type: DataType = DataType.STRING
    override fun toString(): String = DataType.STRING.toString()
}
object AnalysisVoidType : AnalysisType(), CompleteType {
    override val type: DataType = DataType.VOID
    override fun toString(): String = DataType.VOID.toString()
}
object AnalysisAnythingType : AnalysisType(), CompleteType {
    override val type: DataType = DataType.ANYTHING
    override fun toString(): String = DataType.ANYTHING.toString()
}

data class AnalysisFunctionType(val returnType: AnalysisType, val parameters: AnalysisParamList) : AnalysisType(), CompleteType {

    override val type: DataType = DataType.FUNCTION

    override fun toString(): String {
        val paramList = if(parameters is AnalysisKnownParams) {
            parameters.params.joinToString(", ")
        } else {
            "???"
        }
        return "[$returnType function($paramList)]"
    }
}

object AnalysisUnknownType : AnalysisType() {
    override fun toString(): String = "?"
}

data class AnalysisIntersectionType(val possible: Set<AnalysisType>): AnalysisType() {
    override fun toString(): String = buildString {
        append('<')
        append(possible.joinToString(" | "))
        append('>')
    }
}

sealed class AnalysisParamList
class AnalysisKnownParams(val params: List<AnalysisType>) : AnalysisParamList() {
    val size = params.size
}
object AnalysisUnknownParams : AnalysisParamList()