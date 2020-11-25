int factorial(int n, int accum) {
    if(n == 1) return accum;
    return factorial(n - 1, n * accum);
}

while(1) {
    auto n = readInt();
    if(n < 0) {
        break;
    }
    printInt(factorial(n, 1));
    printString("\n");
}