package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable
import com.tobi.mc.parser.util.getComponents

class SyntaxValidator(val rules: List<SyntaxRule>) {

    fun validateSyntax(computable: Computable) {
        for (rule in rules) {
            rule.validate(computable)
        }
        computable.getComponents().forEach(this::validateSyntax)
    }
}