package com.tobi.mc.parser;

import com.google.gson.Gson;

public class Node {

    public NodeType type;
    public transient int typeId;

    public Node left;
    public Node right;

    public String lexeme;
    public int value;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
