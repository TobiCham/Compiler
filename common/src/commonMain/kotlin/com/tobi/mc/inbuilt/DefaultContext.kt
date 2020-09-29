package com.tobi.mc.inbuilt

import com.tobi.mc.computable.Context

class DefaultContext : Context(null) {

    init {
        defineVariable("printInt", FunctionPrintInt)
        defineVariable("printString", FunctionPrintString)
        defineVariable("readInt", FunctionReadInt)
        defineVariable("readString", FunctionReadString)
        defineVariable("intToString", FunctionIntToString)
        defineVariable("concat", FunctionConcat)
        defineVariable("unixTime", FunctionUnixTime)
        defineVariable("sleep", FunctionSleep)
    }
}