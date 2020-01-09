package com.tobi.mc.parser;

public class Parser {

    /**
     * Takes input from stdin
     * @return The generated node, or null if there was an error
     */
    public native Node parseProgram();
}
