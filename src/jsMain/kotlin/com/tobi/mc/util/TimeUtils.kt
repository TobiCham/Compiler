package com.tobi.mc.util

import kotlin.js.Date

actual object TimeUtils {
    actual fun unixTimeMillis(): Long = Date.now().toLong()
}