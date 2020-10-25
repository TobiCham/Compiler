package com.tobi.mc.parser.syntax

import com.tobi.mc.parser.syntax.rules.*

object SyntaxRules {

    val ALL_RULES: List<SyntaxRule> = listOf(
        RuleBreakAndContinueMustBeInsideLoop,
        RuleDivideByZero,
        RuleFunctionsMustReturn,
        RuleVariableExists,
        RuleFunctionPrototypes
    )
}