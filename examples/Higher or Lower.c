auto makeRandom() {
	auto start = unixTime();
	auto multiplier = 1103515245;
	auto increment = 12345;
	auto mod = 9235357;

	auto gen(int max) {
		auto result = 0;
		start = ((start * multiplier) + increment) % mod;

		if(max > 0) result = start % max;
		else result = start;

		if(result < 0) {
			result = result * -1;
		}
		return result;
	}
	gen(-1);
	return gen;
}

auto readMaximum() {
	while(1) {
		auto inp = readInt();
		if(inp <= 0) {
			printString("Invalid maximum. Enter again:\n");
			continue;
		}
		return inp;
	}
}

auto readGuess() {
	printString("Make a guess!:\n");
	return readInt();
}

auto printHowToPlay() {
	printString("Higher or lower!\nTry to guess the number in as few guesses as possible!\n");
	printString("Enter a maximum:\n");
}

auto printGuesses(int guesses) {
    if(guesses == 1) {
        printString("1 guess");
    } else {
        printInt(guesses);
        printString(" guesses");
    }
}

printHowToPlay();

auto gen = makeRandom();
auto maximum = readMaximum();
auto numb = gen(maximum);

auto guesses = 1;
while(1) {
	auto guess = readGuess();
	if(guess == numb) break;

	if(guess < numb) {
		printString("Higher!\n");
	} else {
		printString("Lower!\n");
	}
	guesses = guesses + 1;
}

printString("Correct! You took ");
printGuesses(guesses);
printString("!\nThanks for playing!\n");
