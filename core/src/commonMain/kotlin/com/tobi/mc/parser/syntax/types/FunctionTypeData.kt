package com.tobi.mc.parser.syntax.types

import com.tobi.mc.computable.FunctionDeclaration
import com.tobi.util.TypeNameConverter

class FunctionTypeData(val function: FunctionDeclaration) {

    val returnTypes: Set<AnalysisType> = LinkedHashSet()

    fun addReturnType(type: AnalysisType) = apply {
        (returnTypes as MutableSet).add(type)
    }

    fun findCommonReturnType(): AnalysisType? = findCommonReturnType(this.returnTypes)

    @Suppress("UNCHECKED_CAST")
    private fun findCommonReturnType(types: Set<AnalysisType>): AnalysisType? {
        if(types.isEmpty()) return null
        if(types.size == 1) return types.first()
        return types.reduce(TypeMerger::mergeTypes)
    }
}

object TypeMerger {

    fun invokeFunction(function: AnalysisType, args: List<AnalysisType>): AnalysisType {
        val mapFunction: (AnalysisType) -> AnalysisFunctionType = {
            if(it !is AnalysisFunctionType) {
                throw IllegalStateException("Cannot invoke ${TypeNameConverter.getTypeName(it)}")
            }
            it
        }
        val possibleFunctions = when(function) {
            is AnalysisIntersectionType -> function.possible.map(mapFunction)
            else -> listOf(mapFunction(function))
        }
        var resultType: AnalysisType? = null
        for(func in possibleFunctions) {
            if(canInvoke(func, args)) {
                if(resultType == null) resultType = func.returnType
                else resultType = mergeTypes(resultType, func.returnType)
            }
        }
        return resultType ?: throw IllegalStateException("Cannot invoke that function with those arguments")
    }

    private fun canInvoke(function: AnalysisFunctionType, args: List<AnalysisType>): Boolean = when(function.parameters) {
        is AnalysisUnknownParams -> true
        is AnalysisKnownParams -> {
            val params = function.parameters.params
            params.size == args.size && params.withIndex().all { (i, type) -> canBeAssignedTo(type, args[i]) }
        }
    }

    fun canBeAssignedTo(expectedType: AnalysisType, actualType: AnalysisType): Boolean {
        //TODO Improve this drastically
        return TypeNameConverter.getTypeName(expectedType) == TypeNameConverter.getTypeName(actualType)
    }

    fun mergeTypes(type1: AnalysisType, type2: AnalysisType): AnalysisType {
        if(type1 == type2) return type1
        if(type1 is AnalysisFunctionType && type2 is AnalysisFunctionType) {
            //Functions are the same expect for the return type, and both return types are functions
            if(type1.parameters == type2.parameters && type1.returnType is AnalysisFunctionType && type2.returnType is AnalysisFunctionType) {
                return AnalysisFunctionType(mergeTypes(type1.returnType, type2.returnType), type1.parameters)
            }

            //Functions Have the same number of params and param types
            if(type1.parameters is AnalysisKnownParams && type2.parameters is AnalysisKnownParams && type1.parameters.size == type2.parameters.size && type2.returnType is AnalysisFunctionType) {
                val type1Params = type1.parameters.params
                val type2Params = type2.parameters.params

                if(type1Params == type2Params) {
                    val returnType = mergeTypes(type1.returnType, type2.returnType)
                    val params = List(type1.parameters.size) {
                        mergeTypes(type1Params[it], type2Params[it])
                    }
                    return AnalysisFunctionType(returnType, AnalysisKnownParams(params))
                }
            }
        }

        val intersectionSet = LinkedHashSet<AnalysisType>()

        if(type1 is AnalysisIntersectionType) intersectionSet.addAll(type1.possible)
        else intersectionSet.add(type1)

        if(type2 is AnalysisIntersectionType) intersectionSet.addAll(type2.possible)
        else intersectionSet.add(type2)

        if(intersectionSet.contains(AnalysisAnythingType)) {
            return AnalysisAnythingType
        }
        if(intersectionSet.size == 1) return intersectionSet.first()

        return AnalysisIntersectionType(intersectionSet)
    }
}