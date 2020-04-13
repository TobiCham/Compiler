package com.tobi.mc.computable.data

import com.tobi.mc.computable.Context
import com.tobi.mc.computable.Data
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.ParameterList

class DataTypeClosure(
    val parameters: ParameterList,
    val context: Context,
    val body: ExpressionSequence,
    val returnType: DataType?
) : Data() {

    override val type: DataType = DataType.FUNCTION

    override val description: String = "function<$returnType>(${parameters.joinToString(", ") { "${it.type} ${it.name}" }})"

    override fun toScriptString(): String {
        val paramList = parameters.joinToString(", ") {
            "${it.type} ${it.name}"
        }
        return "[${returnType.toString()} function($paramList)]"
    }

    override fun toString(): String = description
}