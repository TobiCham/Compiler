package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.TacBlock

interface TacLabels {

    fun shouldGenerateLabel(block: TacBlock): Boolean

    fun getLabel(block: TacBlock): String?

    fun generateNewLabel(): String
}