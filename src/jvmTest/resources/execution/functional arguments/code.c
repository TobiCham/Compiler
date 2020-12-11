auto readInputs(function callback) {
    while(1) {
        auto n = readInt();
        if(n < 0) {
            return;
        }
        callback(n);
    }
}

auto printSquared(int n) {
    printInt(n * n);
    printString("\n");
}

readInputs(printSquared);