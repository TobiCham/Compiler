package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.intermediate.construct.TacExpression

sealed class TacVariableReference : TacCodeConstruct, TacExpression
interface TacMutableVariable

class RegisterVariable(val register: Int) : TacVariableReference(), TacMutableVariable
class StringVariable(val stringIndex: Int) : TacVariableReference()
class EnvironmentVariable(val name: String) : TacVariableReference(), TacMutableVariable
class IntValue(val value: Long) : TacVariableReference()
class ParamReference(val index: Int) : TacVariableReference()