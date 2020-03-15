package com.tobi.mc.parser.syntax

import com.tobi.mc.parser.syntax.rules.RuleBreakAndContinueMustBeInsideLoop
import com.tobi.mc.parser.syntax.rules.RuleDivideByZero
import com.tobi.mc.parser.syntax.rules.RuleFunctionsMustReturn
import com.tobi.mc.parser.syntax.rules.RuleVariableExists

internal object SyntaxRulesList {

    val RULES: Array<SyntaxRule<*>> = arrayOf(
        RuleBreakAndContinueMustBeInsideLoop,
        RuleDivideByZero,
        RuleFunctionsMustReturn,
        RuleVariableExists
    )
}