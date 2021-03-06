package com.tobi.mc.computable.operation

import com.tobi.mc.ScriptException
import com.tobi.mc.SourceRange
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataTypeString

data class StringConcat(var str1: Computable, var str2: Computable, override var sourceRange: SourceRange? = null) : Computable {

    override val description: String = "string concatenation"

    override fun getNodes(): Iterable<Computable> = listOf(str1, str2)

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val str1 = this.str1.getString(context, environment)
        val str2 = this.str2.getString(context, environment)

        return DataTypeString(str1 + str2)
    }

    private suspend fun Computable.getString(context: Context, environment: ExecutionEnvironment): String {
        val result = this.compute(context, environment)
        if(result !is DataTypeString) {
            throw ScriptException("Expected string, got ${result.description}", this)
        }
        return result.value
    }
}