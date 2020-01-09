package com.tobi.util

inline fun <T> T?.orElse(other: () -> T): T = this ?: other()