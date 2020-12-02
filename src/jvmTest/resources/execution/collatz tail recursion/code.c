auto collatz(int n) {
    int inner(int n, int tail_n) {
        if(n == 1) return tail_n;
        else if(n % 2 == 0) return inner(n / 2, tail_n + 1);
        else return inner((3 * n) + 1, tail_n + 1);
    }
    return inner(n, 0);
}

auto largest = 1;
auto i = 1;
while(i < 500) {
    auto sum = collatz(i);
    if(sum > largest) {
        largest = sum;
    }
    i = i + 1;
}
printInt(largest);