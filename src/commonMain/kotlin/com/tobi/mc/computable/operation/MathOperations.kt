package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable

data class Add(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("+", Long::plus)
data class Subtract(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("-", Long::minus)
data class Multiply(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("*", Long::times)
data class Divide(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("/", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0", arg2)
    v1 / v2
})
data class Mod(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("%", { v1, v2 ->
    if(v2 == 0L) throw ScriptException("Divide by 0", arg2)
    v1 % v2
})
data class GreaterThan(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(">", { v1, v2 ->
    if(v1 > v2) 1 else 0
})
data class LessThan(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("<", { v1, v2 ->
    if(v1 < v2) 1 else 0
})
data class GreaterThanOrEqualTo(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation(">=", { v1, v2 ->
    if(v1 >= v2) 1 else 0
})
data class LessThanOrEqualTo(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("<=", { v1, v2 ->
    if(v1 <= v2) 1 else 0
})
data class Equals(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("==", { v1, v2 ->
    if(v1 == v2) 1 else 0
})
data class NotEquals(override var arg1: Computable, override var arg2: Computable, override var sourceRange: SourceRange? = null) : MathOperation("!=", { v1, v2 ->
    if(v1 != v2) 1 else 0
})