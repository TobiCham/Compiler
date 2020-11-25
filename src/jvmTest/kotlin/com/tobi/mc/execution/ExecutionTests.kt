package com.tobi.mc.execution

import com.tobi.mc.ParseException
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.Program
import com.tobi.mc.intermediate.TacEmulator
import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.main.createDescriptiveErrorMessage
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.MinusCParser
import com.tobi.mc.parser.ParserConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.StringReader
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class ExecutionTests {

    @Test
    fun `Test parsing and execution of test programs`() {
        val optimisedParser = MinusCParser()
        val unoptimisedParser = MinusCParser(ParserConfiguration(optimisations = emptyList()))

        val files = File(javaClass.classLoader.getResource("execution")!!.file)
        for(file in files.listFiles() ?: emptyArray()) {
            val inputFile = File(file, "input")
            val outputFile = File(file, "output")
            val code = File(file, "code.c").readText()
            val input = if(inputFile.exists()) inputFile.readText() else ""
            val output = if(outputFile.exists()) outputFile.readText() else ""

            testCase(optimisedParser, "Optimised", file.name, code, input, output)
            testCase(unoptimisedParser, "Unoptimised", file.name, code, input, output)
        }
    }

    private fun testCase(parser: MinusCParser, description: String, name: String, code: String, input: String, expectedOutput: String) {
        println("Testing $name ($description):")
        fun <T> doTest(testDescription: String, action: () -> T): T {
            try {
                print(" - ${testDescription}")
                val result = action()
                println(", Pass")
                return result
            } catch (e: Throwable) {
                var msg = "Failed to run '${name}' ($description, $testDescription):"
                if(e is ParseException) {
                    msg += "\n" + e.createDescriptiveErrorMessage(code)
                } else {
                    msg += " " + e.message
                }
                val error = AssertionError(msg)
                error.addSuppressed(e)
                println(", Fail")
                throw error
            }
        }
        val program = doTest("Parse") { parser.parse(code) }
        val tac = doTest("Generate TAC") { TacGenerator.toTac(program) }
        val mips = doTest("Generate MIPS") { TacToMips(MipsConfiguration.StandardMips).toMips(tac) }

        doTest("Emulate program") { testProgramEmulation(program, input, expectedOutput) }
        doTest("Emulate TAC") { testTacEmulation(tac, input, expectedOutput) }
        doTest("Run MIPS") { testMips(name, MipsAssemblyGenerator.generateAssembly(mips), input, expectedOutput) }
    }

    private fun createEnvironment(builder: StringBuilder, input: String) = object : ExecutionEnvironment {
        val reader = BufferedReader(StringReader(input))

        override fun print(message: String) {
            builder.append(message)
        }

        override fun println(message: String) {
            builder.append(message + "\n")
        }

        override suspend fun readLine(): String = reader.readLine()
    }

    private fun testProgramEmulation(program: Program, input: String, expectedOutput: String) {
        val resultOutput = StringBuilder()
        val executionEnvironment = createEnvironment(resultOutput, input)
        runBlocking {
            program.compute(executionEnvironment)
        }
        assertEquals(expectedOutput, resultOutput.toString())
    }

    private fun testTacEmulation(tac: TacProgram, input: String, expectedOutput: String) {
        val resultOutput = StringBuilder()
        val executionEnvironment = createEnvironment(resultOutput, input)
        runBlocking {
            TacEmulator.emulate(tac, executionEnvironment)
        }
        assertEquals(expectedOutput, resultOutput.toString())
    }

    private fun testMips(name: String, mipsCode: String, input: String, expectedOutput: String) {
        val codeFile = File.createTempFile(name, "minusc-test.tmp")
        codeFile.writeText(mipsCode)
        codeFile.deleteOnExit()

        val process = ProcessBuilder("java", "-jar", "mars.jar", "nc", "me", codeFile.absolutePath).start()
        ByteArrayInputStream(input.toByteArray()).use { inp -> inp.copyTo(process.outputStream) }
        process.outputStream.flush()
        process.outputStream.close()
        process.waitFor(30, TimeUnit.SECONDS)

        val processOutput = String(process.inputStream.readAllBytes())
        val errorOutput = String(process.errorStream.readAllBytes())

        if(process.isAlive) {
            process.destroyForcibly()
        }

        if(process.exitValue() != 0) {
            if(!errorOutput.isBlank()) {
                throw AssertionError(errorOutput)
            }
            throw AssertionError("Process exited with code ${process.exitValue()}")
        }

        assertEquals(expectedOutput, processOutput)
    }
}