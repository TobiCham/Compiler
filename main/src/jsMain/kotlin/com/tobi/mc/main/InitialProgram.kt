package com.tobi.mc.main

object InitialProgram {
    val code = """
        auto factorial(int x) {
            if(x <= 2) {
                return x;
            }
            return x * factorial(x - 1);
        }
        
        auto readInput() {
            printString("Enter a number:\n");
            return readInt();
        }
        
        auto x = readInput();
        auto result = factorial(x);
    
        printInt(x);
        printString("! = ");
        printInt(result);
        printString("\n");
    """.trimIndent()
}