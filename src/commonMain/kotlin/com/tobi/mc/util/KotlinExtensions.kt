package com.tobi.mc.util

fun <T> List<T>.copyAndReplaceIndex(index: Int, replacement: T): List<T> {
    val newList = ArrayList<T>(this.size)
    for((i, item) in this.withIndex()) {
        if(i == index) newList.add(replacement)
        else newList.add(item)
    }
    return newList
}

fun <T> MutableCollection<T>.addAll(vararg elements: T) {
    elements.forEach(this::add)
}

operator fun <T> T.plus(collection: Collection<T>): List<T> {
    val list = ArrayList<T>(collection.size + 1)
    list.add(this)
    list.addAll(collection)
    return list
}

inline val Any.typeName: String?
    get() = this::class.simpleName

fun String.escapeForPrinting(): String {
    var result = this
    result = result.replace("\n", "\\n")
    result = result.replace("\t", "\\t")
    result = result.replace("\"", "\\\"")

    return result
}

fun String.toBooleanOrNull(): Boolean? = when(this.toLowerCase()) {
    "true", "yes", "aye aye cap'in" -> true
    "false", "no" -> false
    else -> null
}