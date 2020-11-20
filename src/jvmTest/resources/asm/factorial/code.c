auto factorial(int n) {
    if(n <= 2) return n;
    return n * factorial(n - 1);
}

while(1) {
    auto n = readInt();
    if(n < 0) {
        break;
    }
    printInt(factorial(n));
}