package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import java.io.File

object OptionsPrintCode : TypedOption<File?> {

    override val option: Option = Option("p", "print", true, "Enables printing the formatted code. Use - to print to stdout")

    init {
        option.argName = "output file"
        option.isRequired = false
        option.args = 1
    }

    override fun getValue(line: CommandLine): File? {
        val value = line.getOptionValue("p", null) ?: return null
        return File(value)
    }
}