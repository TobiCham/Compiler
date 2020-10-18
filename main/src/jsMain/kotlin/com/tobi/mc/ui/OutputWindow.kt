package com.tobi.mc.ui

import com.tobi.mc.main.findElement
import com.tobi.mc.main.monaco
import monaco.MonacoHelpers
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLPreElement

class OutputWindow(preId: String, monacoId: String) {

    private val preElement = findElement<HTMLPreElement>(preId)
    private val monacoElement = findElement<HTMLElement>(monacoId)
    private val monacoEditor = MonacoHelpers.createEditor(monacoElement, language = "plaintext", readOnly = true)

    var text: String = ""
        set(value) {
            field = value
            when(type) {
                Type.PLAIN_TEXT -> preElement.innerHTML = value
                Type.RICH_TEXT -> monacoEditor.setValue(value)
            }
        }

    var type = Type.PLAIN_TEXT
        set(value) {
            field = value

            when(value) {
                Type.PLAIN_TEXT -> {
                    preElement.classList.remove("hidden")
                    monacoElement.classList.add("hidden")
                }
                Type.RICH_TEXT -> {
                    preElement.classList.add("hidden")
                    monacoElement.classList.remove("hidden")
                    monacoEditor.layout()
                }
            }
        }

    var language: String = "plaintext"
        set(value) {
            field = value
            monaco.editor.setModelLanguage(monacoEditor.getModel()!!, value)
        }

    fun clear() {
        this.text = ""
    }

    enum class Type {
        PLAIN_TEXT,
        RICH_TEXT
    }
}