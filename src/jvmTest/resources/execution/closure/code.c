auto x = 5;

auto something() {
    void increment(int something) {
        x = x + something;
    }
    return increment;
}

auto func = something();
func(2);
func(8);
func(-3);

printInt(x);