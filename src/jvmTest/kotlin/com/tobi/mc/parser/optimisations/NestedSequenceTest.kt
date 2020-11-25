package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.operation.Add
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.NestedSequenceOptimisation
import org.junit.Test

class NestedSequenceTest {

    @Test
    fun `Ensure nested blocks are optimised correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(NestedSequenceOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput(): ExpressionSequence = ExpressionSequence(listOf(
        VariableDeclaration("a", DataTypeInt(5), DataType.INT),
        VariableDeclaration("b", DataTypeInt(8), DataType.INT),
        ExpressionSequence(listOf(
            VariableDeclaration("a", DataTypeInt(2), DataType.INT),
            VariableDeclaration("f", DataTypeInt(9), DataType.INT),
            FunctionCall(GetVariable("printInt", 5), listOf(
                Add(GetVariable("a", 0), Add(
                    GetVariable("b", 1), GetVariable("f", 0)
                )),
                ExpressionSequence(listOf(
                    GetVariable("f", 1)
                ))
            ))
        )),
        VariableDeclaration("c", DataTypeInt(6), DataType.INT),
        ExpressionSequence(listOf(
            VariableDeclaration("d", DataTypeInt(10), DataType.INT),
            GetVariable("c", 1),
            GetVariable("e", 2)
        )),
        VariableDeclaration("d", DataTypeInt(12), DataType.INT),
    ))

    private fun createOutput(): ExpressionSequence = ExpressionSequence(listOf(
        VariableDeclaration("a", DataTypeInt(5), DataType.INT),
        VariableDeclaration("b", DataTypeInt(8), DataType.INT),
        VariableDeclaration("a0", DataTypeInt(2), DataType.INT),
        VariableDeclaration("f", DataTypeInt(9), DataType.INT),
        FunctionCall(GetVariable("printInt", 4), listOf(
            Add(GetVariable("a0", 0), Add(
                GetVariable("b", 0), GetVariable("f", 0)
            )),
            ExpressionSequence(listOf(
                GetVariable("f", 1)
            ))
        )),
        VariableDeclaration("c", DataTypeInt(6), DataType.INT),
        VariableDeclaration("d0", DataTypeInt(10), DataType.INT),
        GetVariable("c", 0),
        GetVariable("e", 1),
        VariableDeclaration("d", DataTypeInt(12), DataType.INT)
    ))
}