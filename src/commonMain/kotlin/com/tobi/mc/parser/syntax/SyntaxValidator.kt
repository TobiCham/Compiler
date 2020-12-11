package com.tobi.mc.parser.syntax

import com.tobi.mc.computable.Computable

class SyntaxValidator(val rules: List<SyntaxRule>) {

    fun validateSyntax(computable: Computable) {
        for (rule in rules) {
            rule.validate(computable)
        }
        computable.getNodes().forEach(this::validateSyntax)
    }
}