package com.tobi.mc.parser.optimisations

import com.tobi.mc.assertComputablesEqual
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.MinusCParser
import com.tobi.mc.parser.ParserConfiguration
import com.tobi.mc.parser.optimisation.ProgramOptimiser
import com.tobi.mc.parser.optimisation.optimisations.number.AssociativityOptimisation
import com.tobi.mc.removeSourceInformation
import org.junit.Test

class AssociativityTest {

    private val parser = MinusCParser(ParserConfiguration.ONLY_PARSE)

    @Test
    fun `Ensure chains of additions or multiplications are optimised correctly`() {
        val input = createInput()
        val output = ProgramOptimiser(AssociativityOptimisation).optimise(input)

        assertComputablesEqual(createOutput(), output)
    }

    private fun createInput(): Program = parser.parse("""
        int t1 = a + 1 + readInt() + 2 + b;
        int t2 = a * 2 * readInt() * 3 * b;
    """).removeSourceInformation()

    private fun createOutput(): Program = parser.parse("""
        int t1 = 3 + a + readInt() + b;
        int t2 = 6 * a * readInt() * b;
    """).removeSourceInformation()
}