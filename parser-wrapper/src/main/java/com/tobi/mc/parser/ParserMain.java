package com.tobi.mc.parser;

public class ParserMain {

    public static void main(String[] args) {
        System.loadLibrary("parser");
        Node node = new Parser().parseProgram();
        if(node == null) {
            System.exit(1);
            return;
        }
        System.out.println("//RESULT IS: " + node);
    }
}
