package com.tobi.mc.main

import com.tobi.mc.ProgramToString
import com.tobi.mc.computable.Program
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.ParserContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    CodeArea.element.value = InitialProgram.code

    CompileButton.element.addEventListener("click", { compileAndRun() })
    OptimiseButton.element.addEventListener("click", { showOptimisations() })
}

private fun compileAndRun() = handleAction {
    val exitCode = (it.compute(JSExecutionEnvironment) as? DataTypeInt)?.value

    if (OutputWindow.text.isNotEmpty()) OutputWindow.text += "\n"
    OutputWindow.text += when (exitCode) {
        0L -> "Program exited successfully with exit code $exitCode"
        null -> "Program exited successfully"
        else -> "Program exited with code $exitCode"
    }
}

private fun showOptimisations() = handleAction {
    OutputWindow.text =
        ProgramToString(HtmlStyler).toString(it)
}

private fun handleAction(action: suspend (Program) -> Unit) {
    OutputWindow.clear()
    CompileButton.disabled = true
    OptimiseButton.disabled = true

    GlobalScope.launch {
        try {
            parseThen(action)
        } catch (e: Exception) {
            OutputWindow.text += "Unexpected error: ${e.message}"
        } finally {
            CompileButton.disabled = false
            OptimiseButton.disabled = false
        }
    }
}

private suspend fun parseThen(action: suspend (Program) -> Unit) {
    val parserContext = ParserContext.createContext()
    val program = try {
        val program = parserContext.parseFromString(CodeArea.element.value)
        parserContext.processProgram(program)
        program
    } catch (e: Exception) {
        OutputWindow.text += "Failed to parse:\n${e.message}"
        return
    }
    action(program)
}