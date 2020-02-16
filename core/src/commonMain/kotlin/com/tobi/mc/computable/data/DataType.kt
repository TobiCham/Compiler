package com.tobi.mc.computable.data

enum class DataType {

    INT,
    FUNCTION,
    STRING,
    VOID;

    override fun toString(): String {
        return name.toLowerCase()
    }
}