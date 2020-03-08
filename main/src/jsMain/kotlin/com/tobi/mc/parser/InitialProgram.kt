package com.tobi.mc.parser

object InitialProgram {
    val code = """
        auto factorial(int x) {
            if(x <= 2) {
                return x;
            }
            return x * factorial(x - 1);
        }
        
        int readInput() {
            printString("Enter a number:\n");
            return readInt();
        }
        
        void main() {
            auto x = readInput();
            auto result = factorial(x);
        
            printInt(x);
            printString("! = ");
            printInt(result);
        }
    """.trimIndent()
}