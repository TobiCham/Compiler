package monaco

import com.tobi.mc.main.jsObject
import com.tobi.mc.main.monaco
import kotlinx.browser.window
import monaco.editor.IStandaloneCodeEditor
import monaco.editor.IStandaloneEditorConstructionOptions
import org.w3c.dom.HTMLElement

object MonacoHelpers {

    fun createEditor(element: HTMLElement, language: String, theme: String = "vs", value: String = "", readOnly: Boolean = false): IStandaloneCodeEditor {
        val minimap = jsObject {
            this.enabled = false
        }

        val config: IStandaloneEditorConstructionOptions = jsObject {
            this.language = language
            this.theme = theme
            this.value = value
            this.readOnly = readOnly
            this.minimap = minimap
        }
        val editor = monaco.editor.create(element, config)
        registerResizeHandler(editor)
        return editor
    }

    private fun registerResizeHandler(editor: IStandaloneCodeEditor) {
        val debouncer = debounce()
        window.addEventListener("resize", {
            debouncer {
                editor.layout()
            }
        })
    }

    private fun debounce(timeout: Int = 200): (action: () -> Unit) -> Unit {
        var timeoutId: Int? = null
        return { action ->
            if(timeoutId != null) {
                window.clearTimeout(timeoutId!!)
            }
            timeoutId = window.setTimeout(action, timeout)
        }
    }
}