package com.tobi.mc.parser

enum class NodeType {

    IDENTIFIER,
    CONSTANT,
    STRING,
    LE_OP,
    GE_OP,
    EQ_OP,
    NE_OP,
    EXTERN,
    AUTO,
    INT,
    VOID,
    FUNCTION,
    APPLY,
    LEAF,
    IF,
    ELSE,
    WHILE,
    CONTINUE,
    BREAK,
    RETURN,
    COMMA,
    TILDE,
    FUNCTION_DECLARATION,
    FUNCTION_DATA,
    FUNCTION_PARAMS,
    MINUS,
    ADD,
    MULTIPLY,
    DIVIDE,
    MOD,
    GREATER_THAN,
    LESS_THAN,
    SEMI_COLON,
    ASSIGNMENT;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}