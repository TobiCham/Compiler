auto x = 5;

void something() {
    void increment(int something) {
        x = x + something;
    }
}

auto function = something();
function(2);
function(8);
function(-3);

printInt(x);