package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.*

class TacGraphGenerator private constructor() {

    private val names = LinkedHashSet<String>()
    private val connections = LinkedHashSet<String>()

    private val labelMapping = HashMap<TacBlock, String>()

    private fun handleAdd(node: TacNode): String {
        var name = node.nameStr()
        while(!names.add(name)) {
            name += ZERO_WIDTH_CHARACTER
        }
        return name
    }

    private fun TacNode.recurse(
        previous: String?,
        label: String? = null
    ): String {
        fun printConnection(to: String) {
            if(previous != null) {
                connections.add(if(label != null) "$previous $to $label" else "$previous $to")
            }
        }

        if(this is TacBlock) {
            val existingLabel = labelMapping[this]
            if(existingLabel != null) {
                printConnection(existingLabel)
                return existingLabel
            }
            val thisName = handleAdd(this)
            labelMapping[this] = thisName
            printConnection(thisName)

            if(this.instructions.isNotEmpty()) {
                this.instructions.fold(thisName) { previousLabel, tac ->
                    tac.recurse(previousLabel)
                }
            }
            return thisName
        }

        val name = handleAdd(this)
        printConnection(name)

        if(this is TacBranchEqualZero) {
            this.successBlock.recurse(name, "Success")
            this.failBlock.recurse(name, "Fail")
        }

        if(this is TacGoto || this is TacProgram || this is TacFunction) {
            for (node in this.getNodes()) {
                node.recurse(name)
            }
        }
        if(this is TacSetVariable && this.value is TacFunction) {
            this.value.recurse(null)
        }
        return name
    }

    private fun TacNode.nameStr(): String = when(this) {
        is TacBlock -> "Block"
        is TacBranchEqualZero -> "BrZ ${this.conditionVariable.nameStr()}"
        is TacFunctionCall -> "${this.function.nameStr().trim()}()"
        is TacSetArgument -> "SetArg ${this.index} ${this.variable.nameStr()}"
        is TacSetVariable -> "${this.variable.nameStr()}=${this.value.nameStr()}"
        is TacGoto -> "Goto"
        is TacReturn -> "return"
        is TacProgram -> "Program"
        is TacStringConcat -> "Concat ${this.str1.nameStr()} ${this.str2.nameStr()}"
        is TacUnaryMinus -> "-${this.variable.nameStr()}"
        is RegisterVariable -> "r${this.register}"
        is StringVariable -> "STRING ${this.stringIndex}"
        is EnvironmentVariable -> "*(${this.name})"
        is ReturnedValue -> "@return"
        is StackVariable -> this.name
        is IntValue -> this.value.toString()
        is ParamReference -> "Param ${this.index}"
        is TacInbuiltFunction -> "Inbuilt"
        is TacNegation -> "Negate<${this.toNegate}>"
        is TacMathOperation -> "${this.arg1.nameStr()}${this.type.opString}${this.arg2.nameStr()}"
        is TacFunction -> "Func"
        else -> throw IllegalArgumentException("Unknown ${this::class.simpleName}")
    }.replace(' ', NON_SPACE_WHITESPACE)

    companion object {

        private const val NON_SPACE_WHITESPACE = '\u2800'
        private const val ZERO_WIDTH_CHARACTER = '\u200B'

        fun createGraphStructure(tac: TacNode): String {
            val printer = TacGraphGenerator()
            printer.apply {
                tac.recurse(null)
            }
            val builder = StringBuilder()
            printer.names.forEach(builder::appendLine)
            printer.connections.forEach(builder::appendLine)
            return builder.toString()
        }
    }
}