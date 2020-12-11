package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.control.ContinueStatement
import com.tobi.mc.computable.control.IfStatement
import com.tobi.mc.computable.control.ReturnStatement
import com.tobi.mc.computable.control.WhileLoop
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.Parameter
import com.tobi.mc.computable.operation.Equals
import com.tobi.mc.computable.operation.Multiply
import com.tobi.mc.computable.operation.Subtract
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.computable.variable.SetVariable
import com.tobi.mc.computable.variable.VariableDeclaration
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.NestedSequenceOptimisation
import com.tobi.mc.parser.optimisation.optimisations.TailRecursionOptimisation
import org.junit.Test

class TailRecursionTest {

    @Test
    fun `Ensure tail recursion calls are optimised correctly`() {
        val result = ProgramOptimiser(TailRecursionOptimisation, NestedSequenceOptimisation).optimise(createInput())
        val expected = createOutput()
        assertComputablesEqual(expected, result)
    }

    private fun createInput() = FunctionDeclaration(
        "factorial",
        listOf(Parameter(DataType.INT, "n"), Parameter(DataType.INT, "accum")),
        ExpressionSequence(
            IfStatement(Equals(GetVariable("n", 1), DataTypeInt(1)), ExpressionSequence(
                ReturnStatement(GetVariable("accum", 2))
            ), null),
            ReturnStatement(FunctionCall(
                GetVariable("factorial", 2),
                listOf(
                    Subtract(GetVariable("n", 1), DataTypeInt(1)),
                    Multiply(GetVariable("n", 1), GetVariable("accum", 1))
                )
            ))
        ),
        DataType.INT
    )

    private fun createOutput() = FunctionDeclaration(
        "factorial",
        listOf(Parameter(DataType.INT, "n"), Parameter(DataType.INT, "accum")),
        ExpressionSequence(
            WhileLoop(DataTypeInt(1), ExpressionSequence(
                IfStatement(Equals(GetVariable("n", 2), DataTypeInt(1)), ExpressionSequence(
                    ReturnStatement(GetVariable("accum", 3))
                ), null),
                VariableDeclaration("tail_n", Subtract(GetVariable("n", 2), DataTypeInt(1)), DataType.INT),
                VariableDeclaration("tail_accum", Multiply(GetVariable("n", 2), GetVariable("accum", 2)), DataType.INT),
                SetVariable("n", 2, GetVariable("tail_n", 0)),
                SetVariable("accum", 2, GetVariable("tail_accum", 0)),
                ContinueStatement()
            ))
        ),
        DataType.INT
    )
}