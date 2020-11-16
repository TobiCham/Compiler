package com.tobi.mc.mips

sealed class MipsArgument

data class Register(val name: String) : MipsArgument()
data class IndirectRegister(val name: String, val offset: Int): MipsArgument()
data class Label(val label: String): MipsArgument()
data class Value(val value: Int): MipsArgument()