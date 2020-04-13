auto intDivision(int x, int y) {
    int mod = x % y;
    int result = x / y;
    if(mod * 2 >= y) {
        result = result + 1;
    }
    return result;
}

auto squareRoot(int n) {
    auto iterate(int guess) {
        if(guess == 0) {
            return 0;
        } else {
            auto nextGuess = intDivision(guess + intDivision(n, guess), 2);
            if(nextGuess == guess) {
                return guess;
            }
            return iterate(nextGuess);
        }
    }
    return iterate(n);
}

auto _ = printString("Enter a number:\n");
auto value = readInt();
if(value < 0) {
    printString("Not a real number");
} else {
    auto result = squareRoot(value);
    printString("sqrt(");
    printInt(value);
    printString(") = ");
    printInt(result);
}

