package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.RedundantVariablesOptimisation
import org.junit.Test

class RedundantVariablesTest {

    @Test
    fun `Ensure redundant variables are removed and transformed correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(RedundantVariablesOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput() = ExpressionSequence(listOf(
        VariableDeclaration("unusedVar", FunctionCall(GetVariable("printInt", 3), listOf(DataTypeInt(6))), DataType.VOID),
        VariableDeclaration("usedVar", DataTypeInt(8), DataType.INT),
        FunctionDeclaration("unusedFunc", emptyList(), ExpressionSequence(emptyList()), DataType.VOID),
        FunctionDeclaration("usedFunc", emptyList(), ExpressionSequence(emptyList()), DataType.VOID),

        FunctionCall(GetVariable("usedFunc", 0), emptyList()),
        SetVariable("usedVar", 0, DataTypeInt(5))
    ))

    private fun createOutput() = ExpressionSequence(listOf(
        FunctionCall(GetVariable("printInt", 3), listOf(DataTypeInt(6))),
        VariableDeclaration("usedVar", DataTypeInt(8), DataType.INT),
        FunctionDeclaration("usedFunc", emptyList(), ExpressionSequence(emptyList()), DataType.VOID),

        FunctionCall(GetVariable("usedFunc", 0), emptyList()),
        SetVariable("usedVar", 0, DataTypeInt(5))
    ))
}