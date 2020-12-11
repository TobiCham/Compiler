package com.tobi.mc.main.options

import com.tobi.mc.main.TypedOption
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Option
import java.io.File

object OptionGenerateFlowGraph : TypedOption<File?> {

    override val option: Option = Option("f", "flowgraph", true, "Enables generating TAC flow graph. Use - to print to stdout")

    init {
        option.argName = "output file"
        option.isRequired = false
        option.args = 1
    }

    override fun getValue(line: CommandLine): File? {
        val value = line.getOptionValue("f", null) ?: return null
        return File(value)
    }
}