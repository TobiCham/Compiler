package com.tobi.mc.main

import com.tobi.mc.ParseException
import com.tobi.mc.ProgramToString
import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.intermediate.TacGenerator
import com.tobi.mc.intermediate.TacToString
import com.tobi.mc.mips.MipsAssemblyGenerator
import com.tobi.mc.mips.MipsConfiguration
import com.tobi.mc.mips.TacToMips
import com.tobi.mc.parser.MinusCParser
import com.tobi.mc.parser.ParserConfiguration
import com.tobi.mc.ui.OptimisationsButton
import com.tobi.mc.ui.OutputWindow
import com.tobi.mc.ui.SimpleButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import monaco.KeyCode
import monaco.MarkerSeverity
import monaco.MonacoHelpers
import monaco.editor.IActionDescriptor
import monaco.editor.ICodeEditor
import monaco.languages.MarkerData
import org.w3c.dom.HTMLInputElement

class Application {

    private val outputWindow = OutputWindow(OUTPUT_TEXT_ID, OUTPUT_EDITOR_ID)
    private val validateButton = SimpleButton(VALIDATE_ID) {
        val program = parse(false)
        if(program != null) {
            outputWindow.text += "Program validated"
        }
    }
    private val tacButton = SimpleButton(TAC_ID) { createTac() }
    private val mipsButton = SimpleButton(MIPS_ID) { createMips() }
    private val runButton = SimpleButton(RUN_ID) { runProgram() }
    private val prettyPrintButton = SimpleButton(PRETTY_PRINT_ID) { prettyPrint() }
    private val optimiseButton = OptimisationsButton(OPTIMISE_ID)
    private val inputElement = findElement<HTMLInputElement>(INPUT_ID)

    private val editor = MonacoHelpers.createEditor(
        findElement(EDITOR_ID),
        MinusCLanguage.id
    )

    init {
        editor.addAction(object : IActionDescriptor {
            override var id: String = "run"
            override var label: String = "Compile & Run"
            override var keybindings: Array<Int>? = arrayOf(
                monaco.KeyMod.CtrlCmd or KeyCode.F11
            )

            override fun run(editor: ICodeEditor, vararg args: Any) {
                runProgram()
            }
        })
    }

    private fun runProgram() {
        val program = parse(optimiseButton.enabled) ?: return

        setButtonsEnabled(false)
        inputElement.disabled = true
        outputWindow.type = OutputWindow.Type.PLAIN_TEXT
        outputWindow.clear()

        GlobalScope.launch {
            try {
                val environment = JSExecutionEnvironment(outputWindow, inputElement)
                val exitCode = (program.compute(environment) as? DataTypeInt)?.value
                if (outputWindow.text.isNotEmpty()) outputWindow.text += "\n"
                outputWindow.text += when (exitCode) {
                    0L -> "Program exited successfully with exit code $exitCode"
                    null -> "Program exited successfully"
                    else -> "Program exited with code $exitCode"
                }
            } catch (e: ScriptException) {
                if (outputWindow.text.isNotEmpty()) outputWindow.text += "\n"
                outputWindow.text += "Program exited with an error: " + e.message
                if(e.source?.sourceRange != null) {
                    setMarkersFromException(e.message, e.source!!.sourceRange!!)
                }
            } finally {
                setButtonsEnabled(true)
                inputElement.disabled = true
            }
        }
    }

    private fun prettyPrint() {
        val program = parse(optimiseButton.enabled) ?: return
        outputWindow.type = OutputWindow.Type.RICH_TEXT
        outputWindow.text = ProgramToString().toString(program)
        outputWindow.language = "minusc"
    }

    private fun createTac() {
        val program = parse(optimiseButton.enabled) ?: return

        try {
            val tac = TacGenerator(program).toTac()

            outputWindow.type = OutputWindow.Type.RICH_TEXT
            outputWindow.text = TacToString.toString(tac)
            outputWindow.language = "tac"
        } catch (e: Exception) {
            outputWindow.type = OutputWindow.Type.PLAIN_TEXT
            outputWindow.text = "Failed to create TAC: $e"
        }
    }

    private fun createMips() {
        val program = parse(optimiseButton.enabled) ?: return

        val text = try {
            val tac = TacGenerator(program).toTac()
            val mips = TacToMips(MipsConfiguration.StandardMips).toMips(tac)
            MipsAssemblyGenerator.generateAssembly(mips)
        } catch (e: Exception) {
            outputWindow.type = OutputWindow.Type.PLAIN_TEXT
            outputWindow.text = "Failed to create MIPS: $e"
            return
        }

        outputWindow.type = OutputWindow.Type.RICH_TEXT
        outputWindow.text = text
        outputWindow.language = "mips"
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        validateButton.enabled = enabled
        tacButton.enabled = enabled
        mipsButton.enabled = enabled
        runButton.enabled = enabled
        prettyPrintButton.enabled = enabled
    }

    private fun parse(optimise: Boolean): Program? {
        outputWindow.type = OutputWindow.Type.PLAIN_TEXT
        outputWindow.clear()

        val parser = if(optimise) MinusCParser() else MinusCParser(ParserConfiguration(emptyList()))
        val value = editor.getModel()!!.getValue()

        return try {
            val program = parser.parse(value)
            monaco.editor.setModelMarkers(editor.getModel()!!, "minusc", emptyArray())
            program
        } catch (e: ParseException) {
            setMarkersFromException(e.message, e.source)
            outputWindow.text = "Failed to compile: ${e.message}"
            null
        } catch (e: Exception) {
            outputWindow.text = "Unexpected error: $e"
            null
        }
    }

    private fun setMarkersFromException(message: String, src: SourceRange) {
        monaco.editor.setModelMarkers(editor.getModel()!!, "minusc", arrayOf(
            MarkerData(MarkerSeverity.Error, message, src.start.line + 1, src.start.column + 1, src.end.line + 1, src.end.column + 1)
        ))
    }

    private companion object {
        const val EDITOR_ID = "editor"
        const val OUTPUT_TEXT_ID = "output"
        const val OUTPUT_EDITOR_ID = "output-editor"
        const val VALIDATE_ID = "validate"
        const val PRETTY_PRINT_ID = "pretty-print"
        const val TAC_ID = "tac"
        const val MIPS_ID = "mips"
        const val RUN_ID = "run"
        const val OPTIMISE_ID = "optimise"
        const val INPUT_ID = "input"
    }
}