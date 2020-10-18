package com.tobi.mc.parser

import com.tobi.mc.computable.Context
import com.tobi.mc.inbuilt.DefaultContext
import com.tobi.mc.parser.optimisation.Optimisation
import com.tobi.mc.parser.optimisation.Optimisations
import com.tobi.mc.parser.syntax.SyntaxRule
import com.tobi.mc.parser.syntax.SyntaxRules

data class ParserConfiguration(
    /**
     * List of optimisations to perform
     */
    val optimisations: List<Optimisation> = DEFAULT_OPTIMISATIONS,

    val syntaxRules: List<SyntaxRule> = SYNTAX_RULES,

    val defaultContext: Context = DefaultContext()
) {

    companion object {

        val DEFAULT_OPTIMISATIONS: List<Optimisation> = Optimisations.ALL_OPTIMISATIONS

        val SYNTAX_RULES: List<SyntaxRule> = SyntaxRules.ALL_RULES
    }
}