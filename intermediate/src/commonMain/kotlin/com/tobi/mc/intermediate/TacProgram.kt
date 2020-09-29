package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.construct.TacEnvironment
import com.tobi.mc.intermediate.construct.TacFunction

class TacProgram(
    val strings: Array<String>,
    val environments: List<TacEnvironment>,
    val functions: List<TacFunction>
) : TacStructure