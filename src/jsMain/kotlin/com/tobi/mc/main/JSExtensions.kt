package com.tobi.mc.main

import kotlinx.browser.document
import kotlinx.coroutines.await
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.js.Promise

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

external fun escapeRegExp(string: String): String
fun String.escapeRegex(): String = escapeRegExp(this)

inline fun <reified T> findElement(id: String): T {
    val element = document.getElementById(id) ?: throw RuntimeException("Unable to find element $id")
    if(element !is T) {
        throw RuntimeException("Expected ${T::class.simpleName}, got ${element::class.simpleName} with id $id")
    }
    return element
}