int x() {
    return y();
}

auto y() {
    return x();
}