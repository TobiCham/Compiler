package com.tobi.mc

import com.tobi.mc.util.DescriptionMeta

interface Optimisation<T> {

    val description: DescriptionMeta

    fun optimise(item: T): OptimisationResult<T>
}

private val noOp = OptimisationResult.NoOptimisation<Any?>()
private val updateStruc = OptimisationResult.UpdateStructure<Any>()

fun <T> noOptimisation(): OptimisationResult.NoOptimisation<T> = noOp as OptimisationResult.NoOptimisation<T>
fun <T> updateStructure() = updateStruc as OptimisationResult.UpdateStructure<T>
fun <T> newValue(newValue: T?) = OptimisationResult.NewValue(newValue)