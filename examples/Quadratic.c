auto intDivision(int x, int y) {
    int mod = x % y;
    int result = x / y;
    if(mod * 2 >= y) {
        result = result + 1;
    }
    return result;
}

auto squareRoot(int n) {
    int iterate(int guess) {
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
    return iterate(n / 2);
}

auto printResult(int x) {
    printString("x = " ++ intToString(x) ++ "\n");
}

printString("Enter 3 integers representing a quadratic in the form: ax^2 + bx + c = 0:\n");

auto a = readInt();
auto b = readInt();
auto c = readInt();

auto discriminant = (b * b) - (2 * a * c);
if(discriminant < 0) {
    printString("There are no real solutions\n");
    return;
}

if(a == 0) {
    //bx + c = 0
    printResult((c * -1) / b);
} else {
    auto rootedDiscriminant = squareRoot(discriminant);
    auto result1 = ((b * -1) + rootedDiscriminant) / (2 * a);
    auto result2 = ((b * -1) - rootedDiscriminant) / (2 * a);

    printResult(result1);
    printResult(result2);
}

