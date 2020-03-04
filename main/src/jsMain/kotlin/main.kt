import com.tobi.mc.parser.JSExecutionEnvironment
import com.tobi.mc.parser.ParserContext
import com.tobi.mc.parser.ProgramRunner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLTextAreaElement
import kotlin.browser.document

fun main() {
    document.getElementById("compile")!!.addEventListener("click", {
        val area = document.getElementById("code-area") as HTMLTextAreaElement
        process(area.value)
    })
}

private fun process(text: String) {
    val element = document.getElementById("output")!!
    try {
        parse(text)
    } catch(e: Exception) {
        element.innerHTML = "Failed to parse:\n${e.message}"
    }
}

private fun parse(text: String) {
    val parserContext = ParserContext.createContext()
    var ast = parserContext.parseFromString(text)
    ast = parserContext.processProgram(ast)

    val element = document.getElementById("output")!!
    element.innerHTML = ""

    GlobalScope.launch {
        try {
            document.getElementById("compile")!!.setAttribute("disabled", "")
            val exitCode = ProgramRunner().run(ast, JSExecutionEnvironment)
            if(element.innerHTML.isNotEmpty()) element.innerHTML += "\n"
            element.innerHTML += when(exitCode) {
                0 -> "Program exited successfully with exit code $exitCode"
                null -> "Program exited successfully"
                else -> "Program exited with code $exitCode"
            }

        } catch (e: Exception) {
            element.innerHTML = e.message.toString()
        } finally {
            document.getElementById("compile")!!.removeAttribute("disabled")
        }
    }
}