package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option

object OptionRunProgram : TypedOption<Boolean> {

    override val option: Option = Option("r", "run", false, "Executes the program using stdin and stdout")

    init {
        option.isRequired = false
        option.args = 0
    }

    override fun getValue(line: CommandLine): Boolean {
        return line.hasOption("r")
    }
}