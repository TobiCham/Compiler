package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.construct.ControlLabel
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack

class TacGenerationContext {

    private var currentLabel: Int = 0

    val controlLabels: MutableStack<ControlLabel> = ArrayListStack()

    fun resetLabels() {
        currentLabel = 0
    }

    fun generateLabel(): String = "label${currentLabel++}"
}