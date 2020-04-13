package com.tobi.mc.util

actual object TimeUtils {
    actual fun unixTimeMillis(): Long = System.currentTimeMillis()
}