package com.tobi.mc.computable

import com.tobi.mc.ScriptException

class Add(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "+", { v1, v2 -> v1 + v2 })
class Subtract(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "-", { v1, v2 -> v1 - v2 })
class Multiply(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "*", { v1, v2 -> v1 * v2 })
class Divide(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "/", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0 exception")
    v1 / v2
})
class Mod(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "%", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0 exception")
    v1 % v2
})
class GreaterThan(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, ">", { v1, v2 ->
    if(v1 > v2) 1 else 0
})
class LessThan(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "<", { v1, v2 ->
    if(v1 < v2) 1 else 0
})
class GreaterThanOrEqualTo(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, ">=", { v1, v2 ->
    if(v1 >= v2) 1 else 0
})
class LessThanOrEqualTo(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "<=", { v1, v2 ->
    if(v1 <= v2) 1 else 0
})
class Equals(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "==", { v1, v2 ->
    if(v1 == v2) 1 else 0
})
class NotEquals(arg1: Computable, arg2: Computable) : MathOperation(arg1, arg2, "!=", { v1, v2 ->
    if(v1 != v2) 1 else 0
})