package com.tobi.mc.main

import com.tobi.mc.ProgramToString
import com.tobi.mc.intermediate.TacEmulator
import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.intermediate.TacGraphGenerator
import com.tobi.mc.intermediate.TacToString
import com.tobi.mc.main.options.*
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.MinusCParser
import com.tobi.mc.parser.ParserConfiguration
import kotlinx.coroutines.runBlocking
import org.apache.commons.cli.*
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

class MinusCApplication {

    private val parser = GnuParser()
    private val helpFormatter = HelpFormatter()
    private val options = Options()

    init {
        options.addOption(OptionGenerateMips.option)
        options.addOption(OptionGenerateTac.option)
        options.addOption(OptionGenerateFlowGraph.option)
        options.addOption(OptionRunProgram.option)
        options.addOption(OptionsPrintCode.option)
        options.addOption(OptionOptimisations.option)
        options.addOption(OptionShowHelp.option)

        helpFormatter.width = 120
    }

    fun run(args: Array<String>) {
        try {
            runApplication(args)
        } catch (e: ParseException) {
            System.err.println(e.message)
            showHelp()
            exitProcess(1)
        } catch (e: Exception) {
            e.printStackTrace()
            exitProcess(1)
        }
    }

    fun showHelp() {
        helpFormatter.printHelp("minusc [options] <file>", options)
        println("Specify '-' for the input file to read from stdin")
    }

    private fun runApplication(args: Array<String>) {
        val line = parser.parse(options, args)
        if(OptionShowHelp.getValue(line)) {
            showHelp()
            return
        }
        if(line.argList.isEmpty()) {
            throw ParseException("No file specified")
        }
        if(line.argList.size > 1) {
            throw ParseException("Too many files specified")
        }

        val fileText: String
        if(line.argList[0] == "-") {
            fileText = readFromInput()
        } else {
            val file = File(line.argList[0].toString())
            if(!file.exists() || !file.isFile) {
                throw ParseException("Invalid file '${line.argList[0]}'")
            }

            fileText = try {
                file.readText()
            } catch (e: IOException) {
                throw ParseException("Failed to read file: " + e.message)
            }
        }

        val compiler = if(OptionOptimisations.getValue(line)) {
            MinusCParser()
        } else {
            MinusCParser(ParserConfiguration(optimisations = emptyList()))
        }

        val program = try {
            compiler.parse(fileText)
        } catch(e: com.tobi.mc.ParseException) {
            System.err.println("Failed to compile:")
            System.err.println(e.createDescriptiveErrorMessage(fileText))
            exitProcess(1)
        }

        writeToFile(line, OptionsPrintCode) {
            if(OptionsPrintCode.getValue(line)?.name == "-") {
                ProgramToString(JVMConsoleStyler).toString(program)
            } else {
                ProgramToString().toString(program)
            }
        }
        val tacGenerator = if(OptionOptimisations.getValue(line)) TacGenerator() else TacGenerator(emptyList())
        val tac = tacGenerator.toTac(program)

        writeToFile(line, OptionGenerateFlowGraph) {
            TacGraphGenerator.createGraphStructure(tac)
        }
        writeToFile(line, OptionGenerateTac) {
            TacToString.toString(tac)
        }
        writeToFile(line, OptionGenerateMips) {
            val mips = TacToMips(MipsConfiguration.StandardMips).toMips(tac)
            MipsAssemblyGenerator.generateAssembly(mips)
        }

        if(OptionRunProgram.getValue(line)) {
            runBlocking {
                TacEmulator.emulate(tac, JVMExecutionEnvironment)
            }
        }
    }

    private fun readFromInput(): String = buildString {
        while(true) {
            val line = readLine() ?: break
            append(line)
            append('\n')
        }
    }

    private fun writeToFile(line: CommandLine, option: TypedOption<File?>, getContents: () -> String) {
        val file = option.getValue(line) ?: return
        val contents = getContents()

        if(file.toString() == "-") {
            println(contents)
        } else {
            try {
                file.writeText(contents)
            } catch (e: IOException) {
                throw ParseException("Failed to write ${file.name}: ${e.message}")
            }
        }
    }
}