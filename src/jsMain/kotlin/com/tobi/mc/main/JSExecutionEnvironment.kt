package com.tobi.mc.main

import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.ui.OutputWindow
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent

class JSExecutionEnvironment(private val outputWindow: OutputWindow, private val inputElement: HTMLInputElement) : ExecutionEnvironment {

    override fun print(message: String) {
        outputWindow.text += message
    }

    override fun println(message: String) = print("$message\n")

    override suspend fun readLine(): String {
        inputElement.disabled = false
        inputElement.focus()

        try {
            val line = tryReadLine()
            println(line)
            return line
        } finally {
            inputElement.disabled = true
            inputElement.value = ""
        }
    }

    private suspend fun tryReadLine(): String = useListenerPromise(
        ElementListener(inputElement, "keyup") { event, accept, _ ->
            if ((event as KeyboardEvent).keyCode == 13) accept(inputElement.value)
        }
    )
}