package com.tobi.util

expect object TypeNameConverter {

    fun getTypeName(value: Any): String
}