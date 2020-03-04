package com.tobi.util

inline fun <T> T?.orElse(other: () -> T): T = this ?: other()
inline fun <T, S> T?.mapIfNotNull(mapping: (T) -> S): S? = if(this == null) null else mapping(this)
inline fun <T, S> T?.ifNullElse(notNullExp: (T) -> S, nullExp: () -> S): S = if(this != null) notNullExp(this) else nullExp()

fun <T> List<T>.copyExceptIndex(index: Int) = this.filterIndexed { i, t -> i != index }
fun <T> List<T>.copyAndReplaceIndex(index: Int, replacement: T): List<T> {
    val newList = ArrayList<T>(this.size)
    for((i, item) in this.withIndex()) {
        if(i == index) newList.add(replacement)
        else newList.add(item)
    }
    return newList
}
fun <T> List<T>.copyAndReplaceIndex(index: Int, replacement: List<T>): List<T> {
    val newList = ArrayList<T>(this.size)
    for((i, item) in this.withIndex()) {
        if(i == index) newList.addAll(replacement)
        else newList.add(item)
    }
    return newList
}
fun <T> List<T>.getAfterIndex(index: Int): List<T> {
    if(index >= size) return this
    return this.slice(index + 1 until size)
}