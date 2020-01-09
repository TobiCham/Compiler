package com.tobi.mc.parser;

public enum NodeType {

    IDENTIFIER(258, "id"),
    CONSTANT(259),
    STRING(260),
    LE_OP(261, "<="),
    GE_OP(262, ">="),
    EQ_OP(263, "=="),
    NE_OP(264, "!="),
    EXTERN('e', "e"),
    AUTO(266),
    INT(267),
    VOID(268),
    FUNCTION(269),
    APPLY(270),
    LEAF(271),
    IF(272),
    ELSE(273),
    WHILE(274),
    CONTINUE(275),
    BREAK(276),
    RETURN(277),
    COMMA(',', ","),
    TILDE('~', "~"),
    FUNCTION_DECLARATION('D', "D"),
    FUNCTION_DATA('d', "d"),
    FUNCTION_PARAMS('F', "F"),
    MINUS('-', "-"),
    ADD('+', "+"),
    MULTIPLY('*', "*"),
    DIVIDE('/', "/"),
    MOD('%', "%"),
    GREATER_THAN('>', ">"),
    LESS_THAN('<', "<"),
    SEMI_COLON(';', ";"),
    ASSIGNMENT('=', "=");

    //TODO Rename TILDE to something more meaningful

    private int id;
    private String description;

    NodeType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    NodeType(int id) {
        this(id, null);
        this.description = name().toLowerCase();
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static NodeType getById(int id) {
        for(NodeType value : values()) {
            if(value.id == id) {
                return value;
            }
        }
        System.err.println("Unknown node type id " + id);
        System.exit(1);
        return null;
    }
}
