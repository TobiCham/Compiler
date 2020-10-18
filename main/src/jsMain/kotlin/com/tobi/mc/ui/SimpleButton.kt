package com.tobi.mc.ui

import com.tobi.mc.main.findElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent

class SimpleButton(val id: String, onClick: (event: MouseEvent) -> Unit) {

    private val element = findElement<HTMLButtonElement>(id)

    init {
        element.disabled = false
        element.addEventListener("click", {
            onClick(it as MouseEvent)
        })
    }

    var enabled: Boolean = true
        set(value) {
            field = value
            element.disabled = !enabled
        }
}