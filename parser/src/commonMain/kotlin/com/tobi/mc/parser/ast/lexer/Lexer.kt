package com.tobi.mc.parser.ast.lexer

import com.tobi.mc.parser.ParseException
import com.tobi.mc.parser.ast.ReaderHelpers
import com.tobi.mc.parser.ast.SimpleReader
import com.tobi.mc.parser.ast.parser.runtime.FileLocation

internal class Lexer(private val reader: SimpleReader) {
    /**
     * this buffer contains the current text to be matched and is
     * the source of the yytext() string
     */
    private var zzBuffer = CharArray(LexerConstants.ZZ_BUFFERSIZE)
    /**
     * the textposition at the last accepting state
     */
    private var zzMarkedPos = 0
    /**
     * the current text position in the buffer
     */
    private var zzCurrentPos = 0
    /**
     * startRead marks the beginning of the yytext() string in the buffer
     */
    private var zzStartRead = 0
    /**
     * endRead marks the last character in the buffer, that has been read
     * from input
     */
    private var zzEndRead = 0
    /**
     * number of newlines encountered up to the start of the matched text
     */
    private var yyline = 0
    /**
     * the number of characters from the last newline up to the start of the
     * matched text
     */
    private var yycolumn = 0
    /**
     * zzAtEOF == true iff the scanner is at the EOF
     */
    private var zzAtEOF = false
    /**
     * The number of occupied positions in zzBuffer beyond zzEndRead.
     * When a lead/high surrogate has been read from the input stream
     * into the final zzBuffer position, this will have a value of 1;
     * otherwise, it will have a value of 0.
     */
    private var zzFinalHighSurrogate = 0

    private fun makeNode(type: LexerNodeType, value: Any): LexerNode {
        return LexerNode(type, value, yyline, yycolumn)
    }

    /**
     * Refills the input buffer.
     *
     * @return `false`, if there was new input.
     */
    private fun zzRefill(): Boolean { /* first: make room (if you can) */
        if (zzStartRead > 0) {
            zzEndRead += zzFinalHighSurrogate
            zzFinalHighSurrogate = 0
            ReaderHelpers.copyArray(
                zzBuffer,
                zzStartRead,
                zzBuffer,
                0,
                zzEndRead - zzStartRead
            )
            /* translate stored positions */zzEndRead -= zzStartRead
            zzCurrentPos -= zzStartRead
            zzMarkedPos -= zzStartRead
            zzStartRead = 0
        }
        /* is the buffer big enough? */if (zzCurrentPos >= zzBuffer.size - zzFinalHighSurrogate) { /* if not: blow it up */
            val newBuffer = CharArray(zzBuffer.size * 2)
            ReaderHelpers.copyArray(zzBuffer, 0, newBuffer, 0, zzBuffer.size)
            zzBuffer = newBuffer
            zzEndRead += zzFinalHighSurrogate
            zzFinalHighSurrogate = 0
        }
        /* fill the buffer with new input */
        val requested = zzBuffer.size - zzEndRead
        val numRead = reader.read(zzBuffer, zzEndRead, requested)
        /* not supposed to occur according to specification of java.io.Reader */if (numRead == 0) {
            throw RuntimeException("Reader returned 0 characters. See JFlex examples for workaround.")
        }
        if (numRead > 0) {
            zzEndRead += numRead
            /* If numRead == requested, we might have requested to few chars to
                encode a full Unicode character. We assume that a Reader would
                otherwise never return half characters. */
            if (numRead == requested) {
                val ch = zzBuffer[zzEndRead - 1]
                if (ch.isHighSurrogate()) {
                    --zzEndRead
                    zzFinalHighSurrogate = 1
                }
            }
            /* potentially more input available */return false
        }
        /* numRead < 0 ==> end of stream */return true
    }

    fun close() {
        zzAtEOF = true /* indicate end of file */
        zzEndRead = zzStartRead /* invalidate buffer    */
        reader.close()
    }

    /**
     * Returns the text matched by the current regular expression.
     */
    fun yytext(): String {
        return String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead)
    }

    /**
     * Returns the character at position <tt>pos</tt> from the
     * matched text.
     *
     *
     * It is equivalent to yytext().charAt(pos), but faster
     *
     * @param pos the position of the character to fetch.
     * A value from 0 to yylength()-1.
     * @return the character at position pos
     */
    fun yycharat(pos: Int): Char {
        return zzBuffer[zzStartRead + pos]
    }

    /**
     * Returns the length of the matched text region.
     */
    fun yylength(): Int {
        return zzMarkedPos - zzStartRead
    }

    /**
     * Resumes scanning until the next regular expression is matched,
     * the end of input is encountered or an I/O-Error occurs.
     *
     * @return the next token
     */
    fun yylex(): LexerNode? {
        var zzInput: Int
        var zzAction: Int
        // cached fields:
        var zzCurrentPosL: Int
        var zzMarkedPosL: Int
        var zzEndReadL = zzEndRead
        var zzBufferL = zzBuffer
        val zzAttrL = LexerConstants.ZZ_ATTRIBUTE
        while (true) {
            zzMarkedPosL = zzMarkedPos
            var zzR = false
            var zzCh: Int
            var zzCharCount: Int
            zzCurrentPosL = zzStartRead
            while (zzCurrentPosL < zzMarkedPosL) {
                zzCh = ReaderHelpers.codePointAt(
                    zzBufferL,
                    zzCurrentPosL,
                    zzMarkedPosL
                )
                zzCharCount = ReaderHelpers.charCount(zzCh)
                when (zzCh.toChar()) {
                    '\u000B', '\u000C', '\u0085', '\u2028', '\u2029' -> {
                        yyline++
                        yycolumn = 0
                        zzR = false
                    }
                    '\r' -> {
                        yyline++
                        yycolumn = 0
                        zzR = true
                    }
                    '\n' -> if (zzR) zzR = false else {
                        yyline++
                        yycolumn = 0
                    }
                    else -> {
                        zzR = false
                        yycolumn += zzCharCount
                    }
                }
                zzCurrentPosL += zzCharCount
            }
            if (zzR) { // peek one character ahead if it is \n (if we have counted one line too much)
                var zzPeek: Boolean
                if (zzMarkedPosL < zzEndReadL) zzPeek = zzBufferL[zzMarkedPosL] == '\n' else if (zzAtEOF) zzPeek =
                    false else {
                    val eof = zzRefill()
                    zzEndReadL = zzEndRead
                    zzMarkedPosL = zzMarkedPos
                    zzBufferL = zzBuffer
                    zzPeek = if (eof) false else zzBufferL[zzMarkedPosL] == '\n'
                }
                if (zzPeek) yyline--
            }
            zzAction = -1
            zzStartRead = zzMarkedPosL
            zzCurrentPos = zzStartRead
            zzCurrentPosL = zzCurrentPos
            // the current state of the DFA
/*
             * the current lexical state
             */
            var zzState = LexerConstants.ZZ_LEXSTATE[LexerConstants.YYINITIAL]
            // set up zzAction for empty match case:
            var zzAttributes = zzAttrL[zzState]
            if (zzAttributes and 1 == 1) {
                zzAction = zzState
            }
            while (true) {
                if (zzCurrentPosL < zzEndReadL) {
                    zzInput = ReaderHelpers.codePointAt(
                        zzBufferL,
                        zzCurrentPosL,
                        zzEndReadL
                    )
                    zzCurrentPosL += ReaderHelpers.charCount(zzInput)
                } else if (zzAtEOF) {
                    zzInput = LexerConstants.YYEOF
                    break
                } else { // store back cached positions
                    zzCurrentPos = zzCurrentPosL
                    zzMarkedPos = zzMarkedPosL
                    val eof = zzRefill()
                    // get translated positions and possibly new buffer
                    zzCurrentPosL = zzCurrentPos
                    zzMarkedPosL = zzMarkedPos
                    zzBufferL = zzBuffer
                    zzEndReadL = zzEndRead
                    if (eof) {
                        zzInput = LexerConstants.YYEOF
                        break
                    } else {
                        zzInput = ReaderHelpers.codePointAt(
                            zzBufferL,
                            zzCurrentPosL,
                            zzEndReadL
                        )
                        zzCurrentPosL += ReaderHelpers.charCount(zzInput)
                    }
                }
                val zzNext = LexerConstants.ZZ_TRANS[LexerConstants.ZZ_ROWMAP[zzState] + LexerConstants.getCMapAtIndex(zzInput)]
                if (zzNext == -1) break
                zzState = zzNext
                zzAttributes = zzAttrL[zzState]
                if (zzAttributes and 1 == 1) {
                    zzAction = zzState
                    zzMarkedPosL = zzCurrentPosL
                    if (zzAttributes and 8 == 8) break
                }
            }
            // store back cached position
            zzMarkedPos = zzMarkedPosL
            if (zzInput == LexerConstants.YYEOF && zzStartRead == zzCurrentPos) {
                zzAtEOF = true
                return null
            }
            val switchValue = if (zzAction < 0) zzAction else LexerConstants.ZZ_ACTION[zzAction]
            if (switchValue in 37..72) {
                break
            }
            if (switchValue == 1 || switchValue == 2) {
                continue
            }
            return getNode(switchValue)
        }
        return null
    }

    private fun getNode(code: Int): LexerNode {
        if (code == 3) {
            val text = yytext()
            return try {
                makeNode(LexerNodeType.CONSTANT, text.toInt())
            } catch (e: NumberFormatException) {
                throw ParseException("Invalid integer", FileLocation(yyline, yycolumn), FileLocation(yyline, yycolumn + text.length))
            }
        }
        if (code == 4) {
            return makeNode(LexerNodeType.IDENTIFIER, yytext())
        }
        if (code == 22) {
            return makeNode(LexerNodeType.STRING, StringConverter.convertToString(yytext()))
        }
        val typesArray = arrayOf(
            LexerNodeType.ADD,
            LexerNodeType.DIVIDE,
            LexerNodeType.MULTIPLY,
            LexerNodeType.LESS_THAN,
            LexerNodeType.ASSIGNMENT,
            LexerNodeType.GREATER_THAN,
            LexerNodeType.NOT,
            LexerNodeType.SEMI_COLON,
            LexerNodeType.LEFT_CURLY,
            LexerNodeType.RIGHT_CURLY,
            LexerNodeType.COMMA,
            null,
            LexerNodeType.LEFT_BRACKET,
            LexerNodeType.RIGHT_BRACKET,
            LexerNodeType.MINUS,
            LexerNodeType.MOD,
            LexerNodeType.IF,
            null,
            LexerNodeType.LE_OP,
            LexerNodeType.EQ_OP,
            LexerNodeType.GE_OP,
            LexerNodeType.NE_OP,
            LexerNodeType.INT,
            LexerNodeType.AUTO,
            LexerNodeType.ELSE,
            LexerNodeType.VOID,
            LexerNodeType.BREAK,
            LexerNodeType.WHILE,
            LexerNodeType.RETURN,
            LexerNodeType.EXTERN,
            LexerNodeType.FUNCTION,
            LexerNodeType.CONTINUE
        )
        val type = typesArray.getOrNull(code - 5) ?: throw ParseException(LexerConstants.ERROR_NO_MATCH, null, null)
        return LexerNode(type, null, yyline, yycolumn)
    }
}