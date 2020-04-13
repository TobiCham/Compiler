package com.tobi.mc.computable

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.data.DataTypeString

class StringConcat(var str1: DataComputable, var str2: DataComputable) : DataComputable {

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val str1 = this.str1.getString(context, environment)
        val str2 = this.str2.getString(context, environment)

        return DataTypeString(str1 + str2)
    }

    private suspend fun DataComputable.getString(context: Context, environment: ExecutionEnvironment): String {
        val result = this.compute(context, environment)
        if(result !is DataTypeString) {
            throw ScriptException("Expected string, got ${result.description}")
        }
        return result.value
    }

    override fun toString(): String = "$str1 ++ $str2"
}