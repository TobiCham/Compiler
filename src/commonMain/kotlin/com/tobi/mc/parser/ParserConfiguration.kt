package com.tobi.mc.parser

import com.tobi.mc.computable.Context
import com.tobi.mc.inbuilt.DefaultContext
import com.tobi.mc.parser.optimisation.ASTOptimisation
import com.tobi.mc.parser.optimisation.ASTOptimisations
import com.tobi.mc.parser.syntax.SyntaxRule
import com.tobi.mc.parser.syntax.SyntaxRules

data class ParserConfiguration(
    /**
     * List of optimisations to perform
     */
    val optimisations: List<ASTOptimisation> = DEFAULT_OPTIMISATIONS,

    val syntaxRules: List<SyntaxRule> = DEFAULT_SYNTAX_RULES,

    val defaultContext: Context = DefaultContext(),

    val doTypeInference: Boolean = true,

    val resolveIndices: Boolean = true
) {

    companion object {

        val DEFAULT_OPTIMISATIONS: List<ASTOptimisation> = ASTOptimisations.ALL_OPTIMISATIONS

        val DEFAULT_SYNTAX_RULES: List<SyntaxRule> = SyntaxRules.ALL_RULES

        val ONLY_PARSE = ParserConfiguration(
            emptyList(),
            emptyList(),
            doTypeInference = false,
            resolveIndices = false
        )
    }
}