package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import com.tobi.mc.util.toBooleanOrNull
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import org.apache.commons.cli.ParseException

object OptionOptimisations : TypedOption<Boolean> {

    override val option: Option = Option("o", "optimisations", true, "Whether optimisations are enabled. Default true")

    init {
        option.argName = "true/false"
        option.isRequired = false
        option.args = 1
    }

    override fun getValue(line: CommandLine): Boolean {
        val value = line.getOptionValue(option.longOpt, "true")
        return value.toBooleanOrNull() ?: throw ParseException("Invalid boolean '${value}'")
    }
}