package com.tobi.util

actual object TypeNameConverter {

    actual fun getTypeName(value: Any): String = value::class.java.simpleName
}