package com.tobi.mc;

%%

%public
%class Lexer
%type TokenType

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]+

%%

<YYINITIAL> {
    "int" { return TokenType.INT; }
    "string" { return TokenType.STRING; }
    "function" { return TokenType.FUNCTION; }
    "void" { return TokenType.VOID; }
    "anything" { return TokenType.ANYTHING; }
    "???" { return TokenType.UNKNOWN_PARAMS; }
    "?" { return TokenType.UNKNOWN; }
    "[" { return TokenType.START_FUNCTION; }
    "]" { return TokenType.END_FUNCTION; }
    "<" { return TokenType.START_INTERSECTION; }
    ">" { return TokenType.END_INTERSECTION; }
    "|" { return TokenType.OR; }
    "," { return TokenType.PARAM_SEPARATOR; }
    "(" { return TokenType.START_PARAMS; }
    ")" { return TokenType.END_PARAMS; }

    {WhiteSpace} {}
}