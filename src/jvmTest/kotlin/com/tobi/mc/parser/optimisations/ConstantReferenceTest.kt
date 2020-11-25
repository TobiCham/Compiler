package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.ConstantReferenceOptimisation
import org.junit.Test

class ConstantReferenceTest {

    @Test
    fun `Ensure constant references are optimised correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(ConstantReferenceOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput() = ExpressionSequence(listOf(
        VariableDeclaration("a", DataTypeInt(3), DataType.INT),
        VariableDeclaration("b", DataTypeString("Hello"), DataType.INT),
        VariableDeclaration("c", FunctionCall(GetVariable("readInt", 3), emptyList()), DataType.INT),
        VariableDeclaration("d", DataTypeInt(4), DataType.INT),
        GetVariable("a", 0),
        GetVariable("b", 0),
        GetVariable("c", 0),
        GetVariable("d", 0),
        SetVariable("d", 0, DataTypeInt(5))
    ))

    private fun createOutput() = ExpressionSequence(listOf(
        VariableDeclaration("c", FunctionCall(GetVariable("readInt", 3), emptyList()), DataType.INT),
        VariableDeclaration("d", DataTypeInt(4), DataType.INT),
        DataTypeInt(3),
        DataTypeString("Hello"),
        GetVariable("c", 0),
        GetVariable("d", 0),
        SetVariable("d", 0, DataTypeInt(5))
    ))
}