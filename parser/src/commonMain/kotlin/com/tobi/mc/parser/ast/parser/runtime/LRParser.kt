package com.tobi.mc.parser.ast.parser.runtime

import com.tobi.mc.parser.ParseException
import com.tobi.mc.parser.ast.ReaderHelpers
import com.tobi.util.ArrayListStack
import com.tobi.util.Stack

internal abstract class LRParser(
    val scanner: Scanner,
    val symbolFactory: SymbolFactory,
    private val symbolIdNameMapping: (Int) -> String
) {

    abstract fun production_table(): Array<ShortArray>
    abstract fun action_table(): Array<ShortArray>
    abstract fun reduce_table(): Array<ShortArray>
    abstract fun start_state(): Int
    abstract fun start_production(): Int
    abstract fun EOF_sym(): Int
    abstract fun error_sym(): Int
    private var _done_parsing = false
    private var tos = 0
    private var cur_token: Symbol? = null
    private val stack = ArrayListStack<Symbol>()

    private var production_tab: Array<ShortArray> = emptyArray()
    private var action_tab: Array<ShortArray> = emptyArray()
    private var reduce_tab: Array<ShortArray> = emptyArray()

    abstract fun do_action(
        act_num: Int,
        parser: LRParser,
        stack: Stack<Symbol>,
        top: Int
    ): Symbol

    fun scan(): Symbol {
        val sym = scanner.next_token()
        return sym ?: symbolFactory.newSymbol("END_OF_FILE", EOF_sym())
    }

    fun reportFatalError(message: String, info: Any?) {
        _done_parsing = true
        reportError(message, info)
    }

    fun reportError(message: String, info: Any?) {
        if (info is ComplexSymbol) {
            var actualMessage = if(info.symbol == EOF_sym()) "Unexpected end of file" else message
            actualMessage += ". Valid tokens: "
            actualMessage += expectedTokenIds().joinToString(", ", transform = symbolIdNameMapping)

            throw ParseException(
                actualMessage,
                info.locationLeft,
                info.locationRight
            )
        }
    }

    private fun syntaxError(cur_token: Symbol?) {
        reportError("Syntax error", cur_token)
    }

    fun expectedTokenIds(): List<Int> {
        val ret: MutableList<Int> = ArrayList()
        val parseState = stack.peek().parseState
        val row = action_tab[parseState]
        var i = 0
        while (i < row.size) {
            if (row[i].toInt() == -1) {
                i += 2
                continue
            }
            if (!validateExpectedSymbol(row[i].toInt())) {
                i += 2
                continue
            }
            ret.add(row[i].toInt())
            i += 2
        }
        return ret
    }

    private fun validateExpectedSymbol(id: Int): Boolean {
        var lhs: Short
        var rhs_size: Short
        var act: Int
        val vstack = VirtualParseStack(stack)
        /* parse until we fail or get past the lookahead input */while (true) {
            /* look up the action from the current state (on top of stack) */act =
                getAction(vstack.top(), id).toInt()
            /* if its an error, we fail */if (act == 0) return false
            /* > 0 encodes a shift */if (act > 0) { /* push the new state on the stack */
                vstack.push(act - 1)
                /* advance simulated input, if we run off the end, we are done */if (!advancedLookahead()) return true
            } else { /* if this is a reduce with the start production we are done */
                if (-act - 1 == start_production()) return true
                /* get the lhs Symbol and the rhs size */lhs = production_tab[-act - 1][0]
                rhs_size = production_tab[-act - 1][1]
                /* pop handle off the stack */for (i in 0 until rhs_size) vstack.pop()
                vstack.push(getReduce(vstack.top(), lhs.toInt()).toInt())
            }
        }
    }

    private fun getAction(state: Int, sym: Int): Short {
        var tag: Short
        var first: Int
        var last: Int
        var probe: Int
        val row = action_tab[state]
        /* linear search if we are < 10 entries */if (row.size < 20) {
            probe = 0
            while (probe < row.size) {
                /* is this entry labeled with our Symbol or the default? */tag = row[probe++]
                if (tag.toInt() == sym || tag.toInt() == -1) { /* return the next entry */
                    return row[probe]
                }
                probe++
            }
        } else {
            first = 0
            last = (row.size - 1) / 2 - 1 /* leave out trailing default entry */
            while (first <= last) {
                probe = (first + last) / 2
                if (sym == row[probe * 2].toInt()) return row[probe * 2 + 1] else if (sym > row[probe * 2]) first =
                    probe + 1 else last = probe - 1
            }
            /* not found, use the default at the end */return row[row.size - 1]
        }
        /* shouldn't happened, but if we run off the end we return the 
	 default (error == 0) */return 0
    }

    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    private fun getReduce(state: Int, sym: Int): Short {
        var tag: Short
        val row = reduce_tab[state]
        /* if we have a null row we go with the default */
        var probe = 0
        while (probe < row.size) {
            /* is this entry labeled with our Symbol or the default? */tag = row[probe++]
            if (tag.toInt() == sym || tag.toInt() == -1) { /* return the next entry */
                return row[probe]
            }
            probe++
        }
        /* if we run off the end we return the default (error == -1) */return -1
    }

    fun parse(): Symbol { /* the current action code */
        var act: Int
        /* the Symbol/stack element returned by a reduce */
        var lhs_sym: Symbol? = null
        /* information about production being reduced with */
        var handle_size: Short
        var lhs_sym_num: Short

        /* set up direct reference to tables to drive the parser */
        production_tab = production_table()
        action_tab = action_table()
        reduce_tab = reduce_table()
        /* get the first token */cur_token = scan()
        /* push dummy Symbol with start state to get us underway */stack.clear()
        stack.push(symbolFactory.startSymbol("START", 0, start_state()))
        tos = 0
        /* continue until we are told to stop */_done_parsing = false
        while (!_done_parsing) {
            /* Check current token for freshness. */if (cur_token!!.used_by_parser) {
                throw RuntimeException("Symbol recycling detected (fix your scanner).")
            }
            /* current state is always on the top of the stack */ /* look up action out of the current state with the current input */act =
                getAction(stack.peek().parseState, cur_token!!.symbol).toInt()
            /* decode the action -- > 0 encodes shift */if (act > 0) { /* shift to the encoded state by pushing it on the stack */
                cur_token!!.parseState = act - 1
                cur_token!!.used_by_parser = true
                stack.push(cur_token!!)
                tos++
                /* advance to the next Symbol */cur_token = scan()
            } else if (act < 0) { /* perform the action for the reduce */
                lhs_sym = do_action(-act - 1, this, stack, tos)
                /* look up information about the production */
                lhs_sym_num = production_tab[-act - 1][0]
                handle_size = production_tab[-act - 1][1]
                /* pop the handle off the stack */for (i in 0 until handle_size) {
                    stack.pop()
                    tos--
                }
                /* look up the state to go to from the one popped back to */
                act = getReduce(stack.peek().parseState, lhs_sym_num.toInt()).toInt()
                /* shift to that state */lhs_sym.parseState = act
                lhs_sym.used_by_parser = true
                stack.push(lhs_sym)
                tos++
            } else { /* call user syntax error reporting routine */
                syntaxError(cur_token)
                /* try to error recover */if (!errorRecovery()) { /* if that fails give up with a fatal syntax error */
                    reportFatalError("Couldn't repair and continue parse", cur_token)
                    /* just in case that wasn't fatal enough, end parse */_done_parsing = true
                } else {
                    lhs_sym = stack.peek()
                }
            }
        }
        return lhs_sym!!
    }

    private fun errorRecovery(): Boolean { /* first pop the stack back into a state that can shift on error and 
	 do that shift (if that fails, we fail) */
        if (!findRecoveryConfig()) {
            return false
        }
        /* read ahead to create lookahead we can parse multiple times */readLookahead()
        /* repeatedly try to parse forward until we make it the required dist */while (!tryParseAhead()) { /* try to parse forward, if it makes it, bail out of loop */ /* if we are now at EOF, we have failed */
            if (lookahead[0].symbol == EOF_sym()) {
                return false
            }
            /* otherwise, we consume another Symbol and try again */ // BUG FIX by Bruce Hutton
// Computer Science Department, University of Auckland,
// Auckland, New Zealand.
// It is the first token that is being consumed, not the one
// we were up to parsing
            restartLookahead()
        }
        parseLookahead()
        return true
    }
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Determine if we can shift under the special error Symbol out of the
     * state currently on the top of the (real) parse stack.
     */
    private fun shiftUnderError(): Boolean { /* is there a shift under error Symbol */
        return getAction(stack.peek().parseState, error_sym()) > 0
    }

    /**
     * Put the (real) parse stack into error recovery configuration by
     * popping the stack down to a state that can shift on the special
     * error Symbol, then doing the shift.  If no suitable state exists on
     * the stack we return false
     */
    private fun findRecoveryConfig(): Boolean {
        val error_token: Symbol
        val act: Int
        /* Remember the right-position of the top symbol on the stack */
        val right = stack.peek() // TUM 20060327 removed .right
        var left = right // TUM 20060327 removed .left
        /* pop down until we can shift under error Symbol */while (!shiftUnderError()) { /* pop the stack */
            left = stack.pop() // TUM 20060327 removed .left
            tos--
            /* if we have hit bottom, we fail */if (stack.isEmpty()) {
                return false
            }
        }
        /* state on top of the stack can shift under error, find the shift */act =
            getAction(stack.peek().parseState, error_sym()).toInt()
        /* build and shift a special error Symbol */error_token =
            symbolFactory.newSymbol("ERROR", error_sym(), left, right)
        error_token.parseState = act - 1
        error_token.used_by_parser = true
        stack.push(error_token)
        tos++
        return true
    }
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Lookahead Symbols used for attempting error recovery "parse aheads".
     */
    private var lookahead: Array<Symbol> = emptyArray()
    /**
     * Position in lookahead input buffer used for "parse ahead".
     */
    private var lookahead_pos = 0

    private fun readLookahead() { /* create the lookahead array */
        lookahead = Array(ERROR_SYNC_SIZE) {
            val result = cur_token
            cur_token = scan()
            result!!
        }
        /* start at the beginning */lookahead_pos = 0
    }

    private fun cur_err_token(): Symbol? {
        return lookahead[lookahead_pos]
    }

    private fun advancedLookahead(): Boolean { /* advance the input location */
        lookahead_pos++
        /* return true if we didn't go off the end */return lookahead_pos < ERROR_SYNC_SIZE
    }
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Reset the parse ahead input to one Symbol past where we started error
     * recovery (this consumes one new Symbol from the real input).
     */
    private fun restartLookahead() { /* move all the existing input over */
        if (ERROR_SYNC_SIZE - 1 >= 0) {
            ReaderHelpers.copyArray(lookahead, 1, lookahead, 0, ERROR_SYNC_SIZE - 1)
        }
        /* read a new Symbol into the last spot */ // BUG Fix by Bruce Hutton
// Computer Science Department, University of Auckland,
// Auckland, New Zealand. [applied 5-sep-1999 by csa]
// The following two lines were out of order!!
        lookahead[ERROR_SYNC_SIZE - 1] = cur_token!!
        cur_token = scan()
        /* reset our internal position marker */lookahead_pos = 0
    }
    /*. . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Do a simulated parse forward (a "parse ahead") from the current
     * stack configuration using stored lookahead input and a virtual parse
     * stack.  Return true if we make it all the way through the stored
     * lookahead input without error. This basically simulates the action of
     * parse() using only our saved "parse ahead" input, and not executing any
     * actions.
     */
    private fun tryParseAhead(): Boolean {
        var act: Int
        var lhs: Short
        var rhs_size: Short
        /* create a virtual stack from the real parse stack */
        val vstack = VirtualParseStack(stack)
        /* parse until we fail or get past the lookahead input */while (true) { /* look up the action from the current state (on top of stack) */
            act = getAction(vstack.top(), cur_err_token()!!.symbol).toInt()
            /* if its an error, we fail */if (act == 0) return false
            /* > 0 encodes a shift */if (act > 0) { /* push the new state on the stack */
                vstack.push(act - 1)
                /* advance simulated input, if we run off the end, we are done */if (!advancedLookahead()) return true
            } else { /* if this is a reduce with the start production we are done */
                if (-act - 1 == start_production()) {
                    return true
                }
                /* get the lhs Symbol and the rhs size */lhs = production_tab[-act - 1][0]
                rhs_size = production_tab[-act - 1][1]
                /* pop handle off the stack */for (i in 0 until rhs_size) {
                    vstack.pop()
                }
                /* look up goto and push it onto the stack */vstack.push(getReduce(vstack.top(), lhs.toInt()).toInt())
            }
        }
    }

    private fun parseLookahead() { /* the current action code */
        var act: Int
        /* the Symbol/stack element returned by a reduce */
        var lhs_sym: Symbol? = null
        /* information about production being reduced with */
        var handle_size: Short
        var lhs_sym_num: Short
        /* restart the saved input at the beginning */lookahead_pos = 0
        /* continue until we accept or have read all lookahead input */while (!_done_parsing) { /* current state is always on the top of the stack */ /* look up action out of the current state with the current input */
            act = getAction(stack.peek().parseState, cur_err_token()!!.symbol).toInt()
            /* decode the action -- > 0 encodes shift */if (act > 0) { /* shift to the encoded state by pushing it on the stack */
                cur_err_token()!!.parseState = act - 1
                cur_err_token()!!.used_by_parser = true
                stack.push(cur_err_token()!!)
                tos++
                /* advance to the next Symbol, if there is none, we are done */if (!advancedLookahead()) { /* scan next Symbol so we can continue parse */ // BUGFIX by Chris Harris <ckharris@ucsd.edu>:
//   correct a one-off error by commenting out
//   this next line.
/*cur_token = scan();*/ /* go back to normal parser */
                    return
                }
            } else if (act < 0) { /* perform the action for the reduce */
                lhs_sym = do_action(-act - 1, this, stack, tos)
                /* look up information about the production */lhs_sym_num = production_tab[-act - 1][0]
                handle_size = production_tab[-act - 1][1]
                /* pop the handle off the stack */for (i in 0 until handle_size) {
                    stack.pop()
                    tos--
                }
                /* look up the state to go to from the one popped back to */act =
                    getReduce(stack.peek().parseState, lhs_sym_num.toInt()).toInt()
                /* shift to that state */lhs_sym.parseState = act
                lhs_sym.used_by_parser = true
                stack.push(lhs_sym)
                tos++
            } else {
                reportFatalError("Syntax error", lhs_sym)
                return
            }
        }
    }

    fun done_parsing() {
        _done_parsing = true
    }

    companion object {
        private const val ERROR_SYNC_SIZE = 3

        fun unpackFromString(str: String): Array<ShortArray> {
            var n = 0 // location in initialization string
            val size1 = str[n].toInt() shl 16 or str[n + 1].toInt()
            n += 2
            val result = Array(size1) {
                val size2 = str[n].toInt() shl 16 or str[n + 1].toInt()
                n += 2
                ShortArray(size2) {
                    (str[n++] - 2).toShort()
                }
            }
            println("""
                arrayOf(${result.joinToString(", ") { arr -> "shortArrayOf(${arr.joinToString(", ")})" }})
            """.trimIndent())
            return result
        }
    }
}