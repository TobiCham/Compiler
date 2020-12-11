package com.tobi.mc

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype
import com.tobi.mc.parser.util.traverseAllNodes
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
    val nodes1 = c1.getNodes().toList()
    val nodes2 = c2.getNodes().toList()

    if(nodes1.size != nodes2.size) {
        throw AssertionError("Size ${nodes1.size} != ${nodes2.size}")
    }
    for ((i, component) in nodes1.withIndex()) {
        deepEquals(component, nodes2[i])
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

fun <T : Computable> T.removeSourceInformation(): T {
    for (computable in this.traverseAllNodes()) {
        computable.sourceRange = null
        if(computable is FunctionDeclaration) {
            computable.parameters.forEach { it.sourceRange = null }
        }
    }
    return this
}

