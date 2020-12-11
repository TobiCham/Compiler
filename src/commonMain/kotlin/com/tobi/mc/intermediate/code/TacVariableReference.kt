package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

sealed class TacVariableReference : TacExpression {
    override fun getNodes(): Iterable<TacNode> = emptyList()
}
sealed class TacMutableVariable : TacVariableReference()

data class RegisterVariable(var register: Int) : TacMutableVariable()
data class StringVariable(val stringIndex: Int) : TacVariableReference()
data class EnvironmentVariable(val name: String, val index: Int) : TacMutableVariable()
data class StackVariable(val name: String) : TacMutableVariable()
data class IntValue(val value: Long) : TacVariableReference()
data class ParamReference(val index: Int) : TacMutableVariable()
object ReturnedValue : TacMutableVariable() {
    override fun toString(): String = "ReturnedValueRegister"
}