package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Program
import com.tobi.mc.parser.SyntaxValidator
import com.tobi.mc.parser.util.SimpleDescription
import com.tobi.mc.parser.util.getComponents
import com.tobi.mc.util.DescriptionMeta

internal class SyntaxValidatorImpl(private val rules: Array<SyntaxRule<*>>) : SyntaxValidator {

    override val description: DescriptionMeta = SimpleDescription("Syntax Validator", """
        Validates that the program syntax is valid
    """.trimIndent())

    override val syntaxRuleDescriptions: List<DescriptionMeta> = rules.map(SyntaxRule<*>::description)

    override fun processProgram(program: Program) {
        program.validateSyntax()
    }

    private fun Computable.validateSyntax() {
        for (rule in rules) {
            if(rule.accepts(this)) {
                rule.validate(this)
            }
        }
        for(component in this.getComponents()) {
            component.validateSyntax()
        }
    }

    private fun SyntaxRule<*>.validate(computable: Computable) {
        @Suppress("UNCHECKED_CAST")
        this as SyntaxRule<Computable>
        computable.run {
            this.validate()
        }
    }
}