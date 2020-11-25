int f1(int x, int limit, int count) {
    if(count >= limit) {
        return x;
    }
    return f2(x * 2, limit, count + 1);
}

int f2(int x, int limit, int count) {
    return f1(x + 1, limit, count + 1);
}

printInt(f2(5, 3, 0));