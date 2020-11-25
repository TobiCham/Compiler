package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.BranchOptimisation
import org.junit.Test

class BranchTest {

    @Test
    fun `Ensure constant branches are optimised correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(BranchOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput() = ExpressionSequence(listOf(
        IfStatement(DataTypeInt(1), ExpressionSequence(listOf(
            GetVariable("x", 0),
            GetVariable("y", 1),
        )), ExpressionSequence(listOf(
            GetVariable("z", 0),
        ))),
        WhileLoop(DataTypeInt(0), ExpressionSequence(listOf(
            GetVariable("a", 0),
        ))),
        WhileLoop(DataTypeInt(1), ExpressionSequence(listOf(
            GetVariable("b", 0),
        )))
    ))

    private fun createOutput() = ExpressionSequence(listOf(
        ExpressionSequence(listOf(
            GetVariable("x", 0),
            GetVariable("y", 1),
        )),
        ExpressionSequence(emptyList()),
        WhileLoop(DataTypeInt(1), ExpressionSequence(listOf(
            GetVariable("b", 0),
        )))
    ))
}