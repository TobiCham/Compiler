package com.tobi.util

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

class PropertyDelegate<T>(private val prop: KMutableProperty0<T>) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return prop.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        prop.set(value)
    }
}

fun <T> property(prop: KMutableProperty0<T>) = PropertyDelegate(prop)