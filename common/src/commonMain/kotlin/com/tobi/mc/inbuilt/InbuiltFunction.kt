package com.tobi.mc.inbuilt

import com.tobi.mc.SourceRange
import com.tobi.mc.computable.ExecutionEnvironment
import com.tobi.mc.computable.data.Data
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.function.Invocable
import com.tobi.mc.computable.function.Parameter
import com.tobi.mc.type.CompleteType
import com.tobi.mc.type.FunctionType
import com.tobi.mc.type.KnownParameters
import com.tobi.mc.type.TypedComputable
import com.tobi.mc.util.DescriptionMeta

abstract class InbuiltFunction(
    val params: List<Pair<String, CompleteType>>,
    returnType: CompleteType
) : Data(), TypedComputable, Invocable {

    abstract val functionDescription: DescriptionMeta

    final override var sourceRange: SourceRange? = null

    override val type: DataType = DataType.FUNCTION

    override val returnType: DataType = returnType.type

    override val expandedType: FunctionType = FunctionType(returnType, KnownParameters(params.map { it.second }))

    override val parameters: List<Parameter> = ArrayList(params.map { (name, type) ->
        Parameter(type.type, name)
    })

    final override suspend fun invoke(arguments: Array<Data>, environment: ExecutionEnvironment): Data {
        return compute(arguments, environment)
    }

    abstract suspend fun compute(arguments: Array<Data>, environment: ExecutionEnvironment): Data
}