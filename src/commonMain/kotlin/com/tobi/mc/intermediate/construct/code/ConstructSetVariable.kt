package com.tobi.mc.intermediate.construct.code

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.TacExpression

data class ConstructSetVariable(val variable: TacMutableVariable, val value: TacExpression) : TacStructure