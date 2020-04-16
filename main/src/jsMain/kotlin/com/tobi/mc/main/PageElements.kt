package com.tobi.mc.main

import com.tobi.mc.util.property
import org.w3c.dom.*
import kotlin.browser.document

open class PageElement<T : Element>(val id: String) {
    val element: T
        get() = document.getElementById(id) as T

    var disabled by property(element::disabled)
}

object OutputWindow : PageElement<HTMLPreElement>("output") {
    var text by property(element::innerHTML)

    fun clear() {
        text = ""
    }

    fun append(text: String) {
        element.innerHTML += text
    }
}

object CompileButton : PageElement<HTMLButtonElement>("compile")
object OptimiseButton : PageElement<HTMLButtonElement>("optimise")
object GenerateTacButton : PageElement<HTMLButtonElement>("tac")
object FormatButton : PageElement<HTMLButtonElement>("format")

object TextInput : PageElement<HTMLInputElement>("input")
object SubmitInputButton : PageElement<HTMLButtonElement>("inputButton")

object CodeArea : PageElement<HTMLTextAreaElement>("code-area")