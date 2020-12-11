package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.ExpressionSequence
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.computable.function.FunctionCall
import com.tobi.mc.computable.operation.StringConcat
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.StringConcatOptimisation
import org.junit.Test

class StringConcatTest {

    @Test
    fun `Ensure two strings get concatenated together correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(StringConcatOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput() = ExpressionSequence(
        StringConcat(DataTypeString("Hello "), DataTypeString("world")),
        StringConcat(DataTypeString("Hello "), FunctionCall(GetVariable("readString", 2), emptyList()))
    )

    private fun createOutput() = ExpressionSequence(
        DataTypeString("Hello world"),
        StringConcat(DataTypeString("Hello "), FunctionCall(GetVariable("readString", 2), emptyList()))
    )
}