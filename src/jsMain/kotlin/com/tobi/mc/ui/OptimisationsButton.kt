package com.tobi.mc.ui

import com.tobi.mc.main.findElement
import org.w3c.dom.Element

class OptimisationsButton(id: String) {

    private val element = findElement<Element>(id)

    init {
        element.addEventListener("click", {
            this.enabled = !this.enabled
        })
    }

    var enabled: Boolean
        get() = element.classList.contains("active")
        set(value) = if(value) element.classList.add("active") else element.classList.remove("active")
}