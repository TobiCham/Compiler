void something(int x) {
    printInt(x);

    return;
}

auto x = 5;
while(x >= 0) {
    x = x - 1;
}

printString("Hello World" ++ "\n");
auto z = !(1 + 2 - 3 * 4 / 5 % 6 > 7 < 8 >= 9 <= 10 == 11 != 12);

while(1) {
    if(unixTime() % 10 == 0) {
        continue;
        break;
    } else {
        break;
    }
}

if(!(2 != x)) {
    printString("idk");
}