package com.tobi.mc.intermediate.construct

import com.tobi.mc.computable.data.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TacEnvironmentTest {

    @Test
    fun `Environments variables should collapse in the correct order`() {
        val env1 = TacEnvironment("", null).apply {
            addVariable("x", DataType.INT)
            addVariable("b", DataType.STRING)
        }
        val env2 = TacEnvironment("", env1).apply {
            addVariable("x", DataType.FUNCTION)
            addVariable("y", DataType.INT)
            addVariable("z", DataType.STRING)
        }
        val env3 = TacEnvironment("", env2).apply {
            addVariable("c", DataType.INT)
            addVariable("y", DataType.FUNCTION)
        }
        assertTrue(env3.getVariableOffsets().contentEquals(arrayOf("b", "x", "z", "c", "y")))
        assertEquals(env3.getVariableOffsetsAsMap(), mapOf("b" to 0, "x" to 1, "z" to 2, "c" to 3, "y" to 4))
    }

    @Test
    fun `Environments shouldn't allow inserting void types`() {
        assertThrows<IllegalArgumentException> {
            TacEnvironment("", null).apply {
                addVariable("void", DataType.VOID)
            }
        }
    }
}