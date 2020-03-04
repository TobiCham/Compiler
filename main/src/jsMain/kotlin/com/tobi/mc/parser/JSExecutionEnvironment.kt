package com.tobi.mc.parser

import com.tobi.mc.computable.ExecutionEnvironment
import kotlinx.coroutines.await
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.js.Promise

object JSExecutionEnvironment : ExecutionEnvironment {

    override fun print(message: String) {
        document.getElementById("output")!!.innerHTML += message
    }

    override fun println(message: String) = print("$message\n")

    override suspend fun readLine(): String {
        val input = document.getElementById("input") as HTMLInputElement
        val button = document.getElementById("inputButton") as HTMLButtonElement

        input.removeAttribute("disabled")
        button.removeAttribute("disabled")

        val promise = Promise<String> { resolve, reject ->
            fun resolveInput() {
                input.setAttribute("disabled", "")
                button.setAttribute("disabled", "")

                val value = input.value
                input.value = ""

                resolve(value)
            }

            var inputEvent: ((Event) -> Unit)? = null
            var buttonEvent: ((Event) -> Unit)? = null

            inputEvent = {
                it as KeyboardEvent
                if(it.keyCode == 13) {
                    input.removeEventListener("keyup", inputEvent)
                    button.removeEventListener("click", buttonEvent)
                    resolveInput()
                }
            }
            buttonEvent = {
                input.removeEventListener("keyup", inputEvent)
                button.removeEventListener("click", buttonEvent)
                resolveInput()
            }
            input.addEventListener("keyup", inputEvent)
            button.addEventListener("click", buttonEvent)
        }
        return promise.await()
    }
}