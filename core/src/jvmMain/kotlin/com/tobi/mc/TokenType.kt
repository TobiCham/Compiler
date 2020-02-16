package com.tobi.mc

enum class TokenType {

    INT,
    STRING,
    VOID,
    ANYTHING,
    UNKNOWN,
    START_FUNCTION,
    END_FUNCTION,
    START_INTERSECTION,
    END_INTERSECTION,
    OR,
    START_PARAMS,
    END_PARAMS,
//    PARAM_NAME,
    PARAM_SEPARATOR,
    UNKNOWN_PARAMS
}