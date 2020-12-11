package com.tobi.mc.parser.types

import com.tobi.mc.ParseException
import com.tobi.mc.SourceObject
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.type.*

object TypeMerger {

    fun invokeFunction(sourceObject: SourceObject, function: ExpandedType, args: List<ExpandedType>): ExpandedType {
        if(function is IntType || function is StringType || function is VoidType || function is UnknownType) {
            throw ParseException("Cannot invoke as a function", sourceObject)
        }

        val possibleFunctions = when(function) {
            is IntersectionType -> function.possible.filterIsInstance<FunctionType>()
            is FunctionType -> listOf(function)
            else -> emptyList()
        }
        var resultType: ExpandedType? = null
        for(func in possibleFunctions) {
            if(canInvoke(func, args)) {
                resultType = if(resultType == null) {
                    func.returnType
                } else {
                    mergeTypes(resultType, func.returnType)
                }
            }
        }
        return resultType
            ?: throw ParseException("Cannot invoke function with the specified arguments", sourceObject)
    }

    private fun canInvoke(function: FunctionType, args: List<ExpandedType>): Boolean = when(function.parameters) {
        is UnknownParameters -> true
        is KnownParameters -> {
            val params = function.parameters.params
            params.size == args.size && params.withIndex().all { (i, type) ->
                canBeAssignedTo(
                    type,
                    args[i]
                )
            }
        }
    }

    fun canBeAssignedTo(expectedType: ExpandedType, actualType: ExpandedType): Boolean = when {
        expectedType is CompleteType && actualType is CompleteType -> expectedType.type == actualType.type
        expectedType is UnknownType || actualType is UnknownType -> true
        actualType is IntersectionType -> actualType.possible.any {
            canBeAssignedTo(
                expectedType,
                it
            )
        }
        expectedType is IntersectionType -> expectedType.possible.any {
            canBeAssignedTo(
                it,
                actualType
            )
        }
        else -> expectedType == actualType
    }

    fun restrictType(sourceObject: SourceObject, type: ExpandedType, restrictTo: DataType): ExpandedType {
        if(type is IntersectionType) {
            val newTypes = type.possible.filter {
                it is CompleteType && it.type == restrictTo
            }
            return if(newTypes.size == 1) {
                newTypes.first()
            } else {
                IntersectionType(newTypes.toSet())
            }
        }
        if(type is CompleteType && type.type == restrictTo) {
            return type
        }
        throw ParseException("Unable to narrow type from $type to $restrictTo", sourceObject)
    }

    fun canBeAssignedToStrict(expectedType: ExpandedType, actualType: ExpandedType): Boolean {
        when {
            expectedType == actualType -> return true
            expectedType === UnknownType || actualType === UnknownType -> return true
            actualType is IntersectionType -> return actualType.possible.all {
                canBeAssignedToStrict(
                    expectedType,
                    it
                )
            }
            expectedType is IntersectionType -> return expectedType.possible.any {
                canBeAssignedToStrict(
                    it,
                    actualType
                )
            }
            expectedType is FunctionType || actualType is FunctionType -> {
                if (expectedType !is FunctionType || actualType !is FunctionType) return false
                if (!canBeAssignedToStrict(
                        expectedType.returnType,
                        actualType.returnType
                    )
                ) return false

                val expectedParams = expectedType.parameters
                val actualParams = actualType.parameters
                if (expectedParams !is KnownParameters || actualParams !is KnownParameters) return true
                return expectedParams.params.size == actualParams.params.size && expectedParams.params.withIndex().all { (i, value) ->
                    canBeAssignedToStrict(
                        value,
                        expectedParams.params[i]
                    )
                }
            }
            else -> return false
        }
    }

    fun mergeTypes(type1: ExpandedType, type2: ExpandedType): ExpandedType {
        if(type1 == type2) return type1
        if(type1 is FunctionType && type2 is FunctionType) {
            //Functions are the same expect for the return type, and both return types are functions
            if(type1.parameters == type2.parameters) {
                return FunctionType(
                    mergeTypes(
                        type1.returnType,
                        type2.returnType
                    ), type1.parameters
                )
            }

            //Functions Have the same number of params and param types
            if(type1.parameters is KnownParameters && type2.parameters is KnownParameters && type1.parameters.size == type2.parameters.size && type2.returnType is FunctionType) {
                val type1Params = type1.parameters.params
                val type2Params = type2.parameters.params

                if(type1Params == type2Params) {
                    val returnType = mergeTypes(
                        type1.returnType,
                        type2.returnType
                    )
                    val params = List(type1.parameters.size) {
                        mergeTypes(
                            type1Params[it],
                            type2Params[it]
                        )
                    }
                    return FunctionType(
                        returnType,
                        KnownParameters(params)
                    )
                }
            }
        }

        val intersectionSet = LinkedHashSet<ExpandedType>()

        if(type1 is IntersectionType) intersectionSet.addAll(type1.possible)
        else intersectionSet.add(type1)

        if(type2 is IntersectionType) intersectionSet.addAll(type2.possible)
        else intersectionSet.add(type2)

        if(intersectionSet.contains(UnknownType)) {
            return UnknownType
        }
        if(intersectionSet.size == 1) return intersectionSet.first()

        return IntersectionType(intersectionSet)
    }

    fun getSimpleType(type: ExpandedType): DataType? = when (type) {
        is CompleteType -> type.type
        is IntersectionType -> {
            val types = type.possible.mapNotNull { getSimpleType(it) }.toSet()
            if(types.size == 1) types.first() else null
        }
        else -> null
    }
}