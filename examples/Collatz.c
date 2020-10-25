auto collatz(int n) {
    if(n == 1) return 0;
    else if(n % 2 == 0) return 1 + collatz(n / 2);
    else return 1 + collatz((3 * n) + 1);
}

auto t1 = unixTime();
auto largest = 1;
auto i = 1;
while(i < 10000) {
    auto sum = collatz(i);
    if(sum > largest) {
        printString("Largest is ");
        printInt(i);
        printString(" (");
        printInt(sum);
        printString(")\n");
        largest = sum;
    }
    i = i + 1;
}

auto t2 = unixTime();
printInt(t2 - t1);