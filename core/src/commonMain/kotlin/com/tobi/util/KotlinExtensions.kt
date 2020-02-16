package com.tobi.util

inline fun <T> T?.orElse(other: () -> T): T = this ?: other()
inline fun <T, S> T?.mapIfNotNull(mapping: (T) -> S): S? = if(this == null) null else mapping(this)