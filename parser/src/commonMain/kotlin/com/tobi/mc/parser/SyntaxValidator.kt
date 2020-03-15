package com.tobi.mc.parser

import com.tobi.mc.util.DescriptionMeta

interface SyntaxValidator : ParserOperation {

    /**
     * @return Details on all the rules used to validate syntax
     */
    val syntaxRuleDescriptions: List<DescriptionMeta>
}