package tobi;

import static tobi.SimpleNodeType.*;
import java.lang.NumberFormatException;

%%

%public
%class MinusCLexer
%type SimpleNode
%line
%column

%{
    private SimpleNode makeNode(SimpleNodeType type) {
        return makeNode(type, null);
    }
    private SimpleNode makeNode(SimpleNodeType type, Object value) {
        return new SimpleNode(type, value, yyline, yycolumn);
    }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]+

Digit = [0-9]
L =	[a-zA-Z_]
H = [a-fA-F0-9]
E = [Ee][+-]?{Digit}+
FS = (f|F|l|L)
IS = (u|U|l|L)*

Comment = {TraditionalComment} | {EndOfLineComment}

InputCharacter = [^\r\n]
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?

%%
<YYINITIAL> {
    {Comment} {}
    //"/*"			{ comment(); }
    "auto"			{ return makeNode(AUTO); }
    "break"			{ return makeNode(BREAK); }
    "continue"		{ return makeNode(CONTINUE); }
    "else"			{ return makeNode(ELSE); }
    "extern"		{ return makeNode(EXTERN); }
    "if"			{ return makeNode(IF); }
    "int"			{ return makeNode(INT); }
    "function"		{ return makeNode(FUNCTION); }
    "return"		{ return makeNode(RETURN); }
    "void"			{ return makeNode(VOID); }
    "while"			{ return makeNode(WHILE); }

    {L}({L}|{Digit})* { return makeNode(IDENTIFIER, yytext()); }
    {Digit}+{IS}?	  {
      String text = yytext();
      try {
        return makeNode(CONSTANT, Integer.parseInt(text));
      } catch(NumberFormatException e) {
        throw new RuntimeException("Invalid integer " + text);
      }
    }

    \"(\\.|[^\\\"])*\"	{ return makeNode(STRING, yytext()); }

    "<="			{ return makeNode(LE_OP); }
    ">="			{ return makeNode(GE_OP); }
    "=="			{ return makeNode(EQ_OP); }
    "!="			{ return makeNode(NE_OP); }
    ";"			{ return makeNode(SEMI_COLON); }
    "{"     		{ return makeNode(LEFT_CURLY); }
    "}"     		{ return makeNode(RIGHT_CURLY); }
    ","			{ return makeNode(COMMA); }
    ":"			{ return makeNode(COLON); }
    "="			{ return makeNode(ASSIGNMENT); }
    "("			{ return makeNode(LEFT_BRACKET); }
    ")"			{ return makeNode(RIGHT_BRACKET); }
    "!"			{ return makeNode(NOT); }
    "-"			{ return makeNode(MINUS); }
    "+"			{ return makeNode(ADD); }
    "*"			{ return makeNode(MULTIPLY); }
    "/"			{ return makeNode(DIVIDE); }
    "%"			{ return makeNode(MOD); }
    "<"			{ return makeNode(LESS_THAN); }
    ">"			{ return makeNode(GREATER_THAN); }

    [ \t\v\n\f]		{}
    .			{ /* ignore bad characters */ }
}