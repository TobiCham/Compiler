package com.tobi.mc.parser.ast.parser

import com.tobi.mc.parser.ast.parser.runtime.ComplexSymbol
import com.tobi.mc.parser.ast.parser.runtime.LRParser
import com.tobi.mc.parser.ast.parser.runtime.Symbol
import com.tobi.util.Stack

/**
 * Cup generated class to encapsulate user supplied action code.
 */
internal class ParserActions {

    /**
     * Method 0 with the actual generated action code for actions 0 to 300.
     */
    fun doAction(
        act_num: Int,
        parser: LRParser,
        stack: Stack<ComplexSymbol>,
        top: Int
    ): Symbol { /* Symbol object for return from actions */
        var parserResult: Symbol
        return when (act_num) {
            0 -> {
                run {
                    val RESULT = (stack.get(top - 1)).value as ParserNode?
                    parserResult = parser.symbolFactory.newSymbol(
                        "\$START",
                        0,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                /* ACCEPT */parser.done_parsing()
                parserResult
            }
            1 -> {
                run {
                    val unitleft = (stack.peek()).left
                    val unitright = (stack.peek()).right
                    val RESULT = (stack.peek()).value as ParserNode?
                    parserResult = parser.symbolFactory.newSymbol(
                        "goal",
                        0,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            2 -> {
                run {
                    var RESULT: ParserNode? = null
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT = ParserNode(ParserNodeType.IDENTIFIER, null, null, (stack.peek()).value)
                    parserResult = parser.symbolFactory.newSymbol(
                        "primary_expression",
                        1,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            3 -> {
                run {
                    var RESULT: ParserNode? = null
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT = ParserNode(ParserNodeType.CONSTANT, null, null, (stack.peek()).value)
                    parserResult = parser.symbolFactory.newSymbol(
                        "primary_expression",
                        1,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            4 -> {
                run {
                    var RESULT: ParserNode? = null
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT = ParserNode(ParserNodeType.STRING, null, null, (stack.peek()).value)
                    parserResult = parser.symbolFactory.newSymbol(
                        "primary_expression",
                        1,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            5 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 1)).left
                    val expright =
                        (stack.get(top - 1)).right
                    val exp =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "primary_expression",
                        1,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            6 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "postfix_expression",
                        2,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            7 -> {
                run {
                    val expleft =
                        (stack.get(top - 2)).left
                    val expright =
                        (stack.get(top - 2)).right
                    val exp =
                        (stack.get(top - 2)).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.APPLY, exp, null)
                    parserResult = parser.symbolFactory.newSymbol(
                        "postfix_expression",
                        2,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            8 -> {
                run {
                    val expleft =
                        (stack.get(top - 3)).left
                    val expright =
                        (stack.get(top - 3)).right
                    val exp =
                        (stack.get(top - 3)).value as ParserNode?
                    val argsleft =
                        (stack.get(top - 1)).left
                    val argsright =
                        (stack.get(top - 1)).right
                    val args =
                        (stack.get(top - 1)).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.APPLY, exp, args)
                    parserResult = parser.symbolFactory.newSymbol(
                        "postfix_expression",
                        2,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            9 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "argument_expression_list",
                        3,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            10 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.PARAMS_SEPARATOR, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "argument_expression_list",
                        3,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            11 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_expression",
                        4,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            12 -> {
                run {
                    var RESULT: ParserNode? = null
                    val opleft =
                        (stack.get(top - 1)).left
                    val opright =
                        (stack.get(top - 1)).right
                    val op =
                        (stack.get(top - 1)).value as ParserNode?
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.INT, op, exp)
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_expression",
                        4,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            13 -> {
                run {
                    var RESULT: ParserNode? = null
                    val opleft = (stack.peek()).left
                    val opright = (stack.peek()).right
                    val op = (stack.peek()).value
                    RESULT = ParserNode(ParserNodeType.MULTIPLY)
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_operator",
                        5,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            14 -> {
                run {
                    var RESULT: ParserNode? = null
                    val opleft = (stack.peek()).left
                    val opright = (stack.peek()).right
                    val op = (stack.peek()).value
                    RESULT = ParserNode(ParserNodeType.ADD)
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_operator",
                        5,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            15 -> {
                run {
                    var RESULT: ParserNode? = null
                    val opleft = (stack.peek()).left
                    val opright = (stack.peek()).right
                    val op = (stack.peek()).value
                    RESULT = ParserNode(ParserNodeType.SUBTRACT)
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_operator",
                        5,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            16 -> {
                run {
                    var RESULT: ParserNode? = null
                    val opleft = (stack.peek()).left
                    val opright = (stack.peek()).right
                    val op = (stack.peek()).value
                    RESULT = ParserNode(ParserNodeType.NOT)
                    parserResult = parser.symbolFactory.newSymbol(
                        "unary_operator",
                        5,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            17 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expressionleft = (stack.peek()).left
                    val expressionright = (stack.peek()).right
                    val expression =
                        (stack.peek()).value as ParserNode?
                    RESULT = expression
                    parserResult = parser.symbolFactory.newSymbol(
                        "multiplicative_expression",
                        6,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            18 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.MULTIPLY, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "multiplicative_expression",
                        6,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            19 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.DIVIDE, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "multiplicative_expression",
                        6,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            20 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.MOD, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "multiplicative_expression",
                        6,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            21 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expressionleft = (stack.peek()).left
                    val expressionright = (stack.peek()).right
                    val expression =
                        (stack.peek()).value as ParserNode?
                    RESULT = expression
                    parserResult = parser.symbolFactory.newSymbol(
                        "additive_expression",
                        7,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            22 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.ADD, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "additive_expression",
                        7,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            23 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.SUBTRACT, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "additive_expression",
                        7,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            24 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "relational_expression",
                        8,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            25 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.LESS_THAN, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "relational_expression",
                        8,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            26 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.MORE_THAN, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "relational_expression",
                        8,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            27 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.LESS_EQ_TO, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "relational_expression",
                        8,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            28 -> {
                run {
                    var RESULT: ParserNode? = null
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.MORE_EQ_TO, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "relational_expression",
                        8,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            29 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "equality_expression",
                        9,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            30 -> {
                run {
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.EQUALS, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "equality_expression",
                        9,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            31 -> {
                run {
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.NOT_EQUALS, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "equality_expression",
                        9,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            32 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "assignment_expression",
                        10,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            33 -> {
                run {
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.ASSIGNMENT, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "assignment_expression",
                        10,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            34 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "expression",
                        11,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            35 -> {
                run {
                    val e1left =
                        (stack.get(top - 2)).left
                    val e1right =
                        (stack.get(top - 2)).right
                    val e1 =
                        (stack.get(top - 2)).value as ParserNode?
                    val e2left = (stack.peek()).left
                    val e2right = (stack.peek()).right
                    val e2 = (stack.peek()).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.PARAMS_SEPARATOR, e1, e2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "expression",
                        11,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            36 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 1)).left
                    val decright =
                        (stack.get(top - 1)).right
                    val dec =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration",
                        12,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            37 -> {
                run {
                    var RESULT: ParserNode? = null
                    val defleft = (stack.peek()).left
                    val defright = (stack.peek()).right
                    val def = (stack.peek()).value as ParserNode?
                    RESULT = def
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration",
                        12,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            38 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.TILDE, dec, list)
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration",
                        12,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            39 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft = (stack.peek()).left
                    val specright = (stack.peek()).right
                    val spec = (stack.peek()).value as ParserNode?
                    RESULT = spec
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_specifiers",
                        13,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            40 -> {
                run {
                    var RESULT: ParserNode? = null
                    val spec1left =
                        (stack.get(top - 1)).left
                    val spec1right =
                        (stack.get(top - 1)).right
                    val spec1 =
                        (stack.get(top - 1)).value as ParserNode?
                    val spec2left = (stack.peek()).left
                    val spec2right = (stack.peek()).right
                    val spec2 = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.TILDE, spec1, spec2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_specifiers",
                        13,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            41 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft = (stack.peek()).left
                    val specright = (stack.peek()).right
                    val spec = (stack.peek()).value as ParserNode?
                    RESULT = spec
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_specifiers",
                        13,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            42 -> {
                run {
                    val spec1 =
                        (stack.get(top - 1)).value as ParserNode?
                    val spec2 = (stack.peek()).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.TILDE, spec1, spec2)
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_specifiers",
                        13,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            43 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "init_declarator_list",
                        14,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            44 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 2)).left
                    val listright =
                        (stack.get(top - 2)).right
                    val list =
                        (stack.get(top - 2)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.PARAMS_SEPARATOR, list, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "init_declarator_list",
                        14,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            45 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "init_declarator",
                        15,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            46 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val expleft = (stack.peek()).left
                    val expright = (stack.peek()).right
                    val exp = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.ASSIGNMENT, dec, exp)
                    parserResult = parser.symbolFactory.newSymbol(
                        "init_declarator",
                        15,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            47 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.EXTERN)
                    parserResult = parser.symbolFactory.newSymbol(
                        "storage_class_specifier",
                        16,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            48 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.AUTO)
                    parserResult = parser.symbolFactory.newSymbol(
                        "storage_class_specifier",
                        16,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            49 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.VOID)
                    parserResult = parser.symbolFactory.newSymbol(
                        "type_specifier",
                        17,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            50 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.INT)
                    parserResult = parser.symbolFactory.newSymbol(
                        "type_specifier",
                        17,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            51 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.FUNCTION)
                    parserResult = parser.symbolFactory.newSymbol(
                        "type_specifier",
                        17,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            52 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "declarator",
                        18,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            53 -> {
                run {
                    var RESULT: ParserNode? = null
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT = ParserNode(ParserNodeType.IDENTIFIER, null, null, (stack.peek()).value)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_declarator",
                        19,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            54 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 1)).left
                    val decright =
                        (stack.get(top - 1)).right
                    val dec =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_declarator",
                        19,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            55 -> {
                run {
                    val decleft =
                        (stack.get(top - 3)).left
                    val decright =
                        (stack.get(top - 3)).right
                    val dec =
                        (stack.get(top - 3)).value as ParserNode?
                    val paramsleft =
                        (stack.get(top - 1)).left
                    val paramsright =
                        (stack.get(top - 1)).right
                    val params =
                        (stack.get(top - 1)).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.FUNCTION_PARAMS, dec, params)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_declarator",
                        19,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            56 -> {
                run {
                    val decleft =
                        (stack.get(top - 3)).left
                    val decright =
                        (stack.get(top - 3)).right
                    val dec =
                        (stack.get(top - 3)).value as ParserNode?
                    val paramsleft =
                        (stack.get(top - 1)).left
                    val paramsright =
                        (stack.get(top - 1)).right
                    val params =
                        (stack.get(top - 1)).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.FUNCTION_PARAMS, dec, params)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_declarator",
                        19,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            57 -> {
                run {
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val RESULT = ParserNode(ParserNodeType.FUNCTION_PARAMS, dec, null)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_declarator",
                        19,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            58 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "parameter_list",
                        20,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            59 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 2)).left
                    val listright =
                        (stack.get(top - 2)).right
                    val list =
                        (stack.get(top - 2)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.PARAMS_SEPARATOR, list, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "parameter_list",
                        20,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            60 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft =
                        (stack.get(top - 1)).left
                    val specright =
                        (stack.get(top - 1)).right
                    val spec =
                        (stack.get(top - 1)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.TILDE, spec, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "parameter_declaration",
                        21,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            61 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft =
                        (stack.get(top - 1)).left
                    val specright =
                        (stack.get(top - 1)).right
                    val spec =
                        (stack.get(top - 1)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.TILDE, spec, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "parameter_declaration",
                        21,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            62 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "parameter_declaration",
                        21,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            63 -> {
                run {
                    var RESULT: ParserNode? = null
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT = ParserNode(ParserNodeType.IDENTIFIER, null, null, (stack.peek()).value)
                    parserResult = parser.symbolFactory.newSymbol(
                        "identifier_list",
                        22,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            64 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 2)).left
                    val listright =
                        (stack.get(top - 2)).right
                    val list =
                        (stack.get(top - 2)).value as ParserNode?
                    val valueleft = (stack.peek()).left
                    val valueright = (stack.peek()).right
                    RESULT =
                        ParserNode(ParserNodeType.PARAMS_SEPARATOR, list, ParserNode(ParserNodeType.IDENTIFIER, null, null, (stack.peek()).value))
                    parserResult = parser.symbolFactory.newSymbol(
                        "identifier_list",
                        22,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            65 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "abstract_declarator",
                        23,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            66 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 1)).left
                    val decright =
                        (stack.get(top - 1)).right
                    val dec =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_abstract_declarator",
                        24,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            67 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = null
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_abstract_declarator",
                        24,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            68 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = list
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_abstract_declarator",
                        24,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            69 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.APPLY, dec, null)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_abstract_declarator",
                        24,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            70 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 3)).left
                    val decright =
                        (stack.get(top - 3)).right
                    val dec =
                        (stack.get(top - 3)).value as ParserNode?
                    val paramsleft =
                        (stack.get(top - 1)).left
                    val paramsright =
                        (stack.get(top - 1)).right
                    val params =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.APPLY, dec, params)
                    parserResult = parser.symbolFactory.newSymbol(
                        "direct_abstract_declarator",
                        24,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            71 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement",
                        25,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            72 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement",
                        25,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            73 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement",
                        25,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            74 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement",
                        25,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            75 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement",
                        25,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            76 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = null
                    parserResult = parser.symbolFactory.newSymbol(
                        "compound_statement",
                        26,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            77 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = list
                    parserResult = parser.symbolFactory.newSymbol(
                        "compound_statement",
                        26,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            78 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = list
                    parserResult = parser.symbolFactory.newSymbol(
                        "compound_statement",
                        26,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            79 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val statementsleft =
                        (stack.get(top - 1)).left
                    val statementsright =
                        (stack.get(top - 1)).right
                    val statements =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.END_STATEMENT, dec, statements)
                    parserResult = parser.symbolFactory.newSymbol(
                        "compound_statement",
                        26,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            80 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_list",
                        27,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            81 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.END_STATEMENT, list, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "declaration_list",
                        27,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            82 -> {
                run {
                    var RESULT: ParserNode? = null
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = statement
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement_list",
                        28,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            83 -> {
                run {
                    var RESULT: ParserNode? = null
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.END_STATEMENT, list, statement)
                    parserResult = parser.symbolFactory.newSymbol(
                        "statement_list",
                        28,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            84 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = null
                    parserResult = parser.symbolFactory.newSymbol(
                        "expression_statement",
                        29,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            85 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 1)).left
                    val expright =
                        (stack.get(top - 1)).right
                    val exp =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = exp
                    parserResult = parser.symbolFactory.newSymbol(
                        "expression_statement",
                        29,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            86 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 2)).left
                    val expright =
                        (stack.get(top - 2)).right
                    val exp =
                        (stack.get(top - 2)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.IF, exp, statement)
                    parserResult = parser.symbolFactory.newSymbol(
                        "selection_statement",
                        30,
                        stack.get(top - 4),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            87 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 4)).left
                    val expright =
                        (stack.get(top - 4)).right
                    val exp =
                        (stack.get(top - 4)).value as ParserNode?
                    val ifsleft =
                        (stack.get(top - 2)).left
                    val ifsright =
                        (stack.get(top - 2)).right
                    val ifs =
                        (stack.get(top - 2)).value as ParserNode?
                    val elsesleft = (stack.peek()).left
                    val elsesright = (stack.peek()).right
                    val elses = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.IF, exp, ParserNode(ParserNodeType.ELSE, ifs, elses))
                    parserResult = parser.symbolFactory.newSymbol(
                        "selection_statement",
                        30,
                        stack.get(top - 6),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            88 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 2)).left
                    val expright =
                        (stack.get(top - 2)).right
                    val exp =
                        (stack.get(top - 2)).value as ParserNode?
                    val bodyleft = (stack.peek()).left
                    val bodyright = (stack.peek()).right
                    val body = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.WHILE, exp, body)
                    parserResult = parser.symbolFactory.newSymbol(
                        "iteration_statement",
                        31,
                        stack.get(top - 4),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            89 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.CONTINUE)
                    parserResult = parser.symbolFactory.newSymbol(
                        "jump_statement",
                        32,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            90 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.BREAK)
                    parserResult = parser.symbolFactory.newSymbol(
                        "jump_statement",
                        32,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            91 -> {
                run {
                    var RESULT: ParserNode? = null
                    RESULT = ParserNode(ParserNodeType.RETURN)
                    parserResult = parser.symbolFactory.newSymbol(
                        "jump_statement",
                        32,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            92 -> {
                run {
                    var RESULT: ParserNode? = null
                    val expleft =
                        (stack.get(top - 1)).left
                    val expright =
                        (stack.get(top - 1)).right
                    val exp =
                        (stack.get(top - 1)).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.RETURN, exp, null)
                    parserResult = parser.symbolFactory.newSymbol(
                        "jump_statement",
                        32,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            93 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "translation_unit",
                        33,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            94 -> {
                run {
                    var RESULT: ParserNode? = null
                    val unitleft =
                        (stack.get(top - 1)).left
                    val unitright =
                        (stack.get(top - 1)).right
                    val unit =
                        (stack.get(top - 1)).value as ParserNode?
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.TILDE, unit, dec)
                    parserResult = parser.symbolFactory.newSymbol(
                        "translation_unit",
                        33,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            95 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft = (stack.peek()).left
                    val decright = (stack.peek()).right
                    val dec = (stack.peek()).value as ParserNode?
                    RESULT = dec
                    parserResult = parser.symbolFactory.newSymbol(
                        "external_declaration",
                        34,
                        stack.peek(),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            96 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft =
                        (stack.get(top - 3)).left
                    val specright =
                        (stack.get(top - 3)).right
                    val spec =
                        (stack.get(top - 3)).value as ParserNode?
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(
                        ParserNodeType.FUNCTION_DEF,
                        ParserNode(ParserNodeType.FUNCTION_DATA, spec, ParserNode(ParserNodeType.EXTERN, dec, list)),
                        statement
                    )
                    parserResult = parser.symbolFactory.newSymbol(
                        "function_definition",
                        35,
                        stack.get(top - 3),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            97 -> {
                run {
                    var RESULT: ParserNode? = null
                    val specleft =
                        (stack.get(top - 2)).left
                    val specright =
                        (stack.get(top - 2)).right
                    val spec =
                        (stack.get(top - 2)).value as ParserNode?
                    val decleft =
                        (stack.get(top - 1)).left
                    val decright =
                        (stack.get(top - 1)).right
                    val dec =
                        (stack.get(top - 1)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(
                        ParserNodeType.FUNCTION_DEF,
                        ParserNode(ParserNodeType.FUNCTION_DATA, spec, dec),
                        statement
                    )
                    parserResult = parser.symbolFactory.newSymbol(
                        "function_definition",
                        35,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            98 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 2)).left
                    val decright =
                        (stack.get(top - 2)).right
                    val dec =
                        (stack.get(top - 2)).value as ParserNode?
                    val listleft =
                        (stack.get(top - 1)).left
                    val listright =
                        (stack.get(top - 1)).right
                    val list =
                        (stack.get(top - 1)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(
                        ParserNodeType.FUNCTION_DEF,
                        ParserNode(ParserNodeType.FUNCTION_DATA, dec, list),
                        statement
                    )
                    parserResult = parser.symbolFactory.newSymbol(
                        "function_definition",
                        35,
                        stack.get(top - 2),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            99 -> {
                run {
                    var RESULT: ParserNode? = null
                    val decleft =
                        (stack.get(top - 1)).left
                    val decright =
                        (stack.get(top - 1)).right
                    val dec =
                        (stack.get(top - 1)).value as ParserNode?
                    val statementleft = (stack.peek()).left
                    val statementright = (stack.peek()).right
                    val statement =
                        (stack.peek()).value as ParserNode?
                    RESULT = ParserNode(ParserNodeType.FUNCTION_DATA, dec, statement)
                    parserResult = parser.symbolFactory.newSymbol(
                        "function_definition",
                        35,
                        stack.get(top - 1),
                        stack.peek(),
                        RESULT
                    )
                }
                parserResult
            }
            else -> throw RuntimeException(
                "Invalid action number " + act_num + "found in internal parse table"
            )
        }
    } /* end of method */

}