package com.tobi.mc.computable

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeVoid
import com.tobi.mc.computable.function.FunctionDeclaration
import com.tobi.mc.computable.function.FunctionPrototype

data class ExpressionSequence(var expressions: MutableList<Computable>, override var sourceRange: SourceRange? = null): Computable {

    constructor(vararg expressions: Computable, sourceRange: SourceRange? = null) : this(arrayListOf(*expressions), sourceRange)

    override val description: String = "code block"

    override fun getNodes(): Iterable<Computable> {
        val nodes = ArrayList<Computable>(this.expressions.size)
        for(expression in expressions) {
            if(expression is FunctionDeclaration) {
                nodes.add(FunctionPrototype(expression))
            }
        }
        nodes.addAll(this.expressions)
        return nodes
    }

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val newContext = Context(context)

        for(expression in expressions) {
            expression.compute(newContext, environment)
        }
        return DataTypeVoid()
    }

    override fun toString(): String = "Block"
}