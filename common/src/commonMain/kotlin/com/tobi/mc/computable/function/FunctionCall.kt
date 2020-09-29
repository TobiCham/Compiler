package com.tobi.mc.computable.function

import com.tobi.mc.ScriptException
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.Context
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.control.FlowInterrupt
import com.tobi.mc.computable.data.Data

class FunctionCall(var function: Computable, var arguments: Array<Computable>) : Computable {

    override val description: String = "function call"

    override suspend fun compute(context: Context, environment: ExecutionEnvironment): Data {
        val function = this.function.compute(context, environment)

        if(function !is Invocable) {
            throw ScriptException("Tried to invoke a function, got ${function.type}")
        }
        checkArgumentsLength(function.parameters)

        val computedArguments = Array(arguments.size) {
            arguments[it].compute(context, environment)
        }
        checkArgumentTypes(computedArguments, function.parameters)

        val result = try {
            function(computedArguments, environment)
        } catch (e: FlowInterrupt.Return) {
            e.toReturn
        } catch (e: FlowInterrupt.Continue) {
            throw ScriptException("Unexpected 'continue' outside of loop")
        } catch (e: FlowInterrupt.Break) {
            throw ScriptException("Unexpected 'break' outside of loop")
        }

        if(function.returnType != result.type) {
            throw ScriptException("Expected to return ${function.returnType}, got ${result.description}")
        }
        return result
    }

    private fun checkArgumentsLength(expectedParameters: List<Parameter>) {
        val expectedSize = expectedParameters.size
        if(arguments.size > expectedSize) {
            throw ScriptException("Too many parameters specified for function. Expected $expectedSize, got ${arguments.size}")
        }
        if(arguments.size < expectedSize) {
            throw ScriptException("Too few parameters specified for function. Expected $expectedSize, got ${arguments.size}")
        }
    }

    private fun checkArgumentTypes(computedArguments: Array<Data>, expectedParameters: List<Parameter>) {
        for(i in computedArguments.indices) {
            val argument = computedArguments[i]
            val parameter = expectedParameters[i]
            if(argument.type !== parameter.type) {
                throw ScriptException("Invalid type on parameter index $i: Expected ${parameter.type}, got ${argument.type}")
            }
        }
    }
}