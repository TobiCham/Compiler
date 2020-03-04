package com.tobi.mc.parser.syntax

internal object SyntaxRulesList {

    val RULES: Array<SyntaxRule<*>> = arrayOf(
        RuleBreakAndContinueMustBeInsideLoop,
        RuleDivideByZero,
        RuleFunctionsMustReturn,
        RuleVariableExists
    )
}