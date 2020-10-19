package com.tobi.mc.intermediate.construct

import com.tobi.mc.intermediate.TacStructure
import com.tobi.mc.intermediate.construct.code.TacCodeConstruct

class TacFunction(
    val name: String,
    val environment: TacEnvironment,
    val code: List<TacCodeConstruct>
) : TacStructure