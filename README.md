# Overview
A functional compiler for a C like language supporting closures and functional arguments to the MIPS assembler language. Written in Kotlin, it supports both a Web interface (using Monaco Editor) and a Desktop command line interface.

# Features
 - Type inference
 - Intermediate code & graph generation
 - Closures & higher order functions
 - [Online interpreter & IDE](https://tobicham.github.io/Compiler/)
 - Program Optimisation (Enable optimisations for [the factorial example](examples/Factorial.c) and pretty print for an example!)

# Demo
A Web IDE can be located on [Github Pages](https://tobicham.github.io/Compiler/). 
All code is run through interpretation, so may be slow for running complex programs within the browser. 

Alternatively, a JVM command line interface can be download [from the releases tab](https://github.com/TobiCham/Compiler/releases). 
Requires Java8+ to run, considering obtaining binaries from [AdoptOpenJDK](https://adoptopenjdk.net/). To run, use:
```bash
java -jar minusc-jvm.jar
```
# Examples
Several examples of programs that can be run can be found in the [examples](examples/) directory.

# Supported Modes
1. Run via interpretation (-r).
The program is evaluated via interpreting the syntax tree on (almost) any hardware.
2. Pretty print (-p).
Outputs code in a more human readable format from the syntax tree. Will replace any `auto` keywords and also show any syntax tree optimisations
3. Output TAC (Three Address Code).
Outputs an intermediate format of the syntax tree, representing the program structure
4. Generate MIPS.
Will output MIPS assembler code for the input program. You may test the output on an available simulator such as the SPIM Simulator, or the MARS MIPS Simulator. 
5. Generate Graph.
Generates graph input data for the program flow graph. Paste into https://csacademy.com/app/graph_editor/ to visualise
6. Optimisations.
Many different optimisations are included, (e.g. tail call elimination, removal of unused or dead code). This flag may be enabled to run all optimisations.

# Brief overview of the language
Written similar to a script, there is no main function, code starts being executed from the top of the file. 

## Data types:
 - int
 - string
 - function (Represents a higher order function, no distinction between types)
 - *void* (for function returns)
 - *auto* (allowing type inference where permitted)

## In built functions:
 - concat
 - exit (Specify an exit code to exit the program early with)
 - intToString
 - printInt
 - printString
 - readInt
 - readString
 - sleep (specify ms to sleep for)
 - unixTime (in ms)

## Allowed constructs:
 - if (0 for false, any other number for true)
 - while
 - break
 - continue
 - return
 - functions & closures

Functions are written in the standard C style, e.g.:
```c
int multiplyByTwo(int x) {
    return x * 2;
}

multiplyByTwo(5); //10

```

## Higher order functions
Higher order functions are declared as regular functions within a function, but can then be used as variables. Functions must be passed as the `function` type when used as a data type. E.g:
```c
function createCounter(int start, function addIncrement) {
    int value = start;
    int step() {
        int toReturn = value;
        value = addIncrement(value);
        return toReturn;
    }
    return step;
}

int addTwo(int value) {
    return value + 2;
}

function counter = createCounter(1, addTwo);
counter(); //1
counter(); //3
counter(); //5

```

## Type inference
Type inference works naturally using the `auto` keyword, and can be used for variable declarations and function return types. E.g.:
```c
auto x = 2; //int
auto y = "hello"; //string

auto timesByTwo(int x) {
    return x * 2; //return type is int
}
```
The pretty print version of this code appears as:
```c
int x = 2;
string y = "hello";

int timesByTwo(int x) {
    return x * 2;
}
```
