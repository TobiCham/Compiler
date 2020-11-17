package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option

object OptionShowHelp : TypedOption<Boolean> {

    override val option: Option = Option("h", "help", false, "Shows help")

    init {
        option.isRequired = false
        option.args = 0
    }

    override fun getValue(line: CommandLine): Boolean {
        return line.hasOption("h")
    }
}