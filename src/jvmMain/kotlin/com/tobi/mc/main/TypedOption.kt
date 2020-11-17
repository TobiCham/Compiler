package com.tobi.mc.main

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option

interface TypedOption<T> {

    val option: Option

    fun getValue(line: CommandLine): T
}

fun <T> CommandLine.getValue(option: TypedOption<T>): T {
    return option.getValue(this)
}