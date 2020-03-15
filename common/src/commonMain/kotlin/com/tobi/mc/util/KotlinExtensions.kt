package com.tobi.mc.util

inline fun <T, S> T?.mapIfNotNull(mapping: (T) -> S): S? = if(this == null) null else mapping(this)

fun <T> List<T>.copyExceptIndex(index: Int) = this.filterIndexed { i, _ -> i != index }
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

inline val Any.typeName: String?
    get() = this::class.simpleName