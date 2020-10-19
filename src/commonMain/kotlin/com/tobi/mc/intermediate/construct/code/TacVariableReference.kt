package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.intermediate.construct.TacExpression

sealed class TacVariableReference : TacCodeConstruct, TacExpression
sealed class TacMutableVariable : TacVariableReference()

class RegisterVariable(var register: Int) : TacMutableVariable()
class StringVariable(val stringIndex: Int) : TacVariableReference()
class EnvironmentVariable(val name: String) : TacMutableVariable()
class IntValue(val value: Long) : TacVariableReference()
class ParamReference(val index: Int) : TacVariableReference()