package com.tobi.mc.parser

import kotlinx.coroutines.await
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.js.Promise

var Element.disabled
    get() = this.hasAttribute("disabled")
    set(value) {
        if(value) this.setAttribute("disabled", "")
        else this.removeAttribute("disabled")
    }

data class ElementListener<T>(val element: HTMLElement, val type: String, val event: (Event, (T) -> Unit, reject: (Throwable) -> Unit) -> Unit)

private data class RawElementListener(val element: HTMLElement, val type: String, val event: (Event) -> Unit)

suspend fun <T> useListenerPromise(vararg events: ElementListener<T>): T {
    val rawListeners = ArrayList<RawElementListener>()

    val returnValue = Promise<T> { resolve, reject ->
        for((element, type, event) in events) {
            val rawListener = RawElementListener(element, type) {
                event(it, resolve, reject)
            }
            rawListeners.add(rawListener)
            element.addEventListener(type, rawListener.event)
        }
    }.await()

    for ((element, type, event) in rawListeners) {
        element.removeEventListener(type, event)
    }

    return returnValue
}
