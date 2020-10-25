auto factorial(int n) {
    int innerFact(int n, int accum) {
        if(n == 1) return accum;
        return innerFact(n - 1, n * accum);
    }
    return innerFact(n, 1);
}

auto n = 1;
while(n <= 2000) {
    factorial(n);
    n = n + 1;
}

