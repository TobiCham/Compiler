package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import java.io.File

object OptionGenerateTac : TypedOption<File?> {

    override val option: Option = Option("t", "tac", true, "Enables generating TAC. Use - to print to stdout")

    init {
        option.argName = "output file"
        option.isRequired = false
        option.args = 1
    }

    override fun getValue(line: CommandLine): File? {
        val value = line.getOptionValue("t", null) ?: return null
        return File(value)
    }
}