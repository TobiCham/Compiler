package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable

class Add(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "+", Long::plus)
class Subtract(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "-", Long::minus)
class Multiply(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "*", Long::times)
class Divide(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "/", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0", arg2)
    v1 / v2
})
class Mod(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "%", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0", arg2)
    v1 % v2
})
class GreaterThan(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, ">", { v1, v2 ->
    if(v1 > v2) 1 else 0
})
class LessThan(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "<", { v1, v2 ->
    if(v1 < v2) 1 else 0
})
class GreaterThanOrEqualTo(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, ">=", { v1, v2 ->
    if(v1 >= v2) 1 else 0
})
class LessThanOrEqualTo(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "<=", { v1, v2 ->
    if(v1 <= v2) 1 else 0
})
class Equals(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "==", { v1, v2 ->
    if(v1 == v2) 1 else 0
})
class NotEquals(arg1: Computable, arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(arg1, arg2, "!=", { v1, v2 ->
    if(v1 != v2) 1 else 0
})