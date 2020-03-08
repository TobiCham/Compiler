package com.tobi.mc.parser

import com.tobi.mc.computable.ExecutionEnvironment
import org.w3c.dom.events.KeyboardEvent

object JSExecutionEnvironment : ExecutionEnvironment {

    override fun print(message: String) {
        OutputWindow.append(message)
    }

    override fun println(message: String) = print("$message\n")

    override suspend fun readLine(): String {
        SubmitInputButton.disabled = false
        TextInput.disabled = false

        try {
            return tryReadLine()
        } finally {
            SubmitInputButton.disabled = true
            TextInput.disabled = true
            TextInput.element.value = ""
        }
    }

    private suspend fun tryReadLine(): String = useListenerPromise(
        ElementListener(TextInput.element, "keyup") { event, accept, _ ->
            if((event as KeyboardEvent).keyCode == 13) accept(TextInput.element.value)
        },
        ElementListener(SubmitInputButton.element, "click") { event, accept, _ ->
            accept(TextInput.element.value)
        }
    )
}