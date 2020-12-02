package com.tobi.mc

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.parser.util.getComponents
import kotlin.test.assertEquals

fun assertComputablesEqual(expected: Computable, actual: Computable) {
    deepEquals(expected, actual)
}

private fun deepEquals(c1: Computable, c2: Computable) {
    if((c1 is FunctionPrototype && c2 is FunctionPrototype) || (c1 is Program && c2 is Program)) {
        return
    }
    if(c1::class != c2::class) {
        throw AssertionError("${c1::class} != ${c2::class}")
    }
    val components1 = c1.getComponents()
    val components2 = c2.getComponents()

    if(components1.size != components2.size) {
        throw AssertionError("Size ${components1.size} != ${components2.size}")
    }
    for ((i, component) in components1.withIndex()) {
        deepEquals(component, components2[i])
    }
    if(c1 != c2) {
        val toString = ProgramToString()
        if(c1 != c2) {
            val str1 = toString.toString(c1)
            val str2 = toString.toString(c2)

            assertEquals(str1, str2)
        }
    }
    assertEquals(c1, c2)
}

