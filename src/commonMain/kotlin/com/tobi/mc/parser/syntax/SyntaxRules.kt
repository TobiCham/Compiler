package com.tobi.mc.parser.syntax

import com.tobi.mc.parser.syntax.rules.RuleBreakAndContinueMustBeInsideLoop
import com.tobi.mc.parser.syntax.rules.RuleDivideByZero
import com.tobi.mc.parser.syntax.rules.RuleFunctionsMustReturn
import com.tobi.mc.parser.syntax.rules.RuleVariableExists

object SyntaxRules {

    val ALL_RULES: List<SyntaxRule> = listOf(
        RuleBreakAndContinueMustBeInsideLoop,
        RuleDivideByZero,
        RuleFunctionsMustReturn,
        RuleVariableExists
    )
}