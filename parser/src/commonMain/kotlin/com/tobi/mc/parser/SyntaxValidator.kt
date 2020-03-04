package com.tobi.mc.parser

import com.tobi.mc.ParseException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.DefaultContext
import com.tobi.mc.computable.Program
import com.tobi.util.DescriptionMeta

interface SyntaxValidator : ParserOperation {

    /**
     * Validates a given computable is valid
     * @param defaultContext Default context of given variables
     * @throws ParseException If a syntax error occurs
     */
    fun validateSyntax(computable: Computable, defaultContext: DefaultContext)

    /**
     * Validates a given program is valid
     * @param defaultContext Default context of given variables
     * @throws ParseException If a syntax error occurs
     */
    fun validateProgram(program: Program, defaultContext: DefaultContext)

    /**
     * @return Details on all the rules used to validate syntax
     */
    val syntaxRuleDescriptions: List<DescriptionMeta>
}