package com.tobi.mc.main

import kotlinx.browser.window
import monaco.Monaco
import monaco._monaco
import monaco.languages.register

val monaco: Monaco
    get() = js("monaco")

fun main() {
    _monaco
    window.onload = {
        loadMonaco()
    }
}

private fun loadMonaco() {
    monaco.languages.register(MinusCLanguage)
    Application()
}

inline fun jsObject(init: dynamic.() -> Unit): dynamic {
    val o = js("{}")
    init(o)
    return o
}