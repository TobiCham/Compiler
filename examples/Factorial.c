auto factorial(int n) {
    int innerFact(int n, int accum) {
        if(n == 1) return accum;
        else return innerFact(n - 1, n * accum);
    }
    return innerFact(n, 1);
}

while(1) {
    auto n = readInt();
    if(n < 0) {
        break;
    }
    printInt(factorial(n));
    printString("\n");
}
