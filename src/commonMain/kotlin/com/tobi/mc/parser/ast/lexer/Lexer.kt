package com.tobi.mc.parser.ast.lexer

import com.tobi.mc.parser.ReaderHelpers
import com.tobi.mc.parser.ast.SimpleReader
import com.tobi.mc.parser.ast.lexer.LexerConstants.OFFSET_NODES
import com.tobi.mc.parser.ast.lexer.LexerConstants.YYEOF
import com.tobi.mc.parser.ast.lexer.LexerConstants.ZZ_ACTION
import com.tobi.mc.parser.ast.lexer.LexerConstants.ZZ_ATTRIBUTE
import com.tobi.mc.parser.ast.lexer.LexerConstants.ZZ_BUFFERSIZE
import com.tobi.mc.parser.ast.lexer.LexerConstants.ZZ_ROWMAP
import com.tobi.mc.parser.ast.lexer.LexerConstants.ZZ_TRANS
import com.tobi.mc.parser.ast.lexer.LexerConstants.zzCMap

class Lexer(
    private val zzReader: SimpleReader
) {
    private var zzBuffer = CharArray(ZZ_BUFFERSIZE)
    private var zzMarkedPos = 0
    private var zzCurrentPos = 0
    private var zzStartRead = 0
    private var zzEndRead = 0
    private var zzAtEOF = false
    private var zzFinalHighSurrogate = 0
    private var yyline = 0
    private var yycolumn = 0

    private fun makeNode(type: LexerNodeType, value: Any? = null): LexerNode {
        return LexerNode(type, yytext(), value, yyline, yycolumn)
    }

    private fun zzRefill(): Boolean {
        if (zzStartRead > 0) {
            zzEndRead += zzFinalHighSurrogate
            zzFinalHighSurrogate = 0
            ReaderHelpers.copyArray(zzBuffer, zzStartRead, zzBuffer, 0, zzEndRead - zzStartRead)
            zzEndRead -= zzStartRead
            zzCurrentPos -= zzStartRead
            zzMarkedPos -= zzStartRead
            zzStartRead = 0
        }
        if (zzCurrentPos >= zzBuffer.size - zzFinalHighSurrogate) {
            val newBuffer = CharArray(zzBuffer.size * 2)
            ReaderHelpers.copyArray(zzBuffer, 0, newBuffer, 0, zzBuffer.size)
            zzBuffer = newBuffer
            zzEndRead += zzFinalHighSurrogate
            zzFinalHighSurrogate = 0
        }
        val requested = zzBuffer.size - zzEndRead
        val numRead = zzReader.read(zzBuffer, zzEndRead, requested)
        if (numRead == 0) {
            throw RuntimeException("Reader returned 0 characters. See JFlex examples/zero-reader for a workaround.")
        }
        if(numRead < 0) {
            return true
        }
        zzEndRead += numRead
        if(zzBuffer[zzEndRead - 1].isHighSurrogate()) {
            if (numRead == requested) { // We requested too few chars to encode a full Unicode character
                --zzEndRead
                zzFinalHighSurrogate = 1
            } else {     // There is room in the buffer for at least one more char
                val c = zzReader.read() // Expecting to read a paired low surrogate char
                if (c == -1) return true
                else zzBuffer[zzEndRead++] = c.toChar()
            }
        }
        return false
    }

    fun close() {
        zzAtEOF = true // indicate end of file
        zzEndRead = zzStartRead // invalidate buffer
        zzReader.close()
    }

    fun yytext(): String {
        return zzBuffer.concatToString(zzStartRead, zzStartRead + (zzMarkedPos - zzStartRead))
    }

    fun yylex(): LexerNode? {
        var zzInput: Int
        var zzAction: Int

        // cached fields:
        var zzCurrentPosL: Int
        var zzMarkedPosL: Int
        var zzEndReadL = zzEndRead
        var zzBufferL = zzBuffer
        val zzAttrL = ZZ_ATTRIBUTE
        while (true) {
            zzMarkedPosL = zzMarkedPos
            var zzR = false
            var zzCh: Int
            var zzCharCount: Int
            zzCurrentPosL = zzStartRead
            while (zzCurrentPosL < zzMarkedPosL) {
                zzCh = ReaderHelpers.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL)
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
            if (zzR) {
                // peek one character ahead if it is
                // (if we have counted one line too much)
                var zzPeek: Boolean
                when {
                    zzMarkedPosL < zzEndReadL -> zzPeek = zzBufferL[zzMarkedPosL] == '\n'
                    zzAtEOF -> zzPeek = false
                    else -> {
                        val eof = zzRefill()
                        zzEndReadL = zzEndRead
                        zzMarkedPosL = zzMarkedPos
                        zzBufferL = zzBuffer
                        zzPeek = if (eof) false else zzBufferL[zzMarkedPosL] == '\n'
                    }
                }
                if (zzPeek) yyline--
            }
            zzAction = -1
            zzStartRead = zzMarkedPosL
            zzCurrentPos = zzStartRead
            zzCurrentPosL = zzCurrentPos
            var zzState = 0

            // set up zzAction for empty match case:
            var zzAttributes = zzAttrL[zzState]
            if (zzAttributes and 1 == 1) {
                zzAction = zzState
            }
            while (true) {
                if (zzCurrentPosL < zzEndReadL) {
                    zzInput = ReaderHelpers.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL)
                    zzCurrentPosL += ReaderHelpers.charCount(zzInput)
                } else if (zzAtEOF) {
                    zzInput = YYEOF
                    break
                } else {
                    // store back cached positions
                    zzCurrentPos = zzCurrentPosL
                    zzMarkedPos = zzMarkedPosL
                    val eof = zzRefill()
                    // get translated positions and possibly new buffer
                    zzCurrentPosL = zzCurrentPos
                    zzMarkedPosL = zzMarkedPos
                    zzBufferL = zzBuffer
                    zzEndReadL = zzEndRead
                    if (eof) {
                        zzInput = YYEOF
                        break
                    } else {
                        zzInput = ReaderHelpers.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL)
                        zzCurrentPosL += ReaderHelpers.charCount(zzInput)
                    }
                }
                val zzNext = ZZ_TRANS[ZZ_ROWMAP[zzState] + zzCMap(zzInput)]
                if (zzNext == -1) break
                zzState = zzNext
                zzAttributes = zzAttrL[zzState]
                if ((zzAttributes and 1) == 1) {
                    zzAction = zzState
                    zzMarkedPosL = zzCurrentPosL
                    if (zzAttributes and 8 == 8) break
                }
            }

            // store back cached position
            zzMarkedPos = zzMarkedPosL
            if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                zzAtEOF = true
                return null
            }
            when(val actualAction = if (zzAction < 0) zzAction else ZZ_ACTION[zzAction]) {
                1, 2 -> {}
                else -> return resolveAction(actualAction)
            }
        }
    }

    private fun resolveAction(action: Int): LexerNode {
        if(action < 0 || action - 3 >= OFFSET_NODES.size) {
            throw RuntimeException("Error: could not match input $action")
        }
        return when(action) {
            12 -> {
                val text = yytext()
                try {
                    makeNode(LexerNodeType.CONSTANT, text.toLong())
                } catch (e: NumberFormatException) {
                    throw RuntimeException("Invalid integer $text")
                }
            }
            17 -> makeNode(LexerNodeType.IDENTIFIER, yytext())
            21 -> {                 //Remove the beginning and trailing quotes from the string
                val text = yytext()
                makeNode(LexerNodeType.TEXT, StringConverter.convertToString(text))
            }
            else -> makeNode(OFFSET_NODES[action - 3]!!)
        }
    }
}