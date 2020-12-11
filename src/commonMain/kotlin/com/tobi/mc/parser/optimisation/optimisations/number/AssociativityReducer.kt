package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.OptimisationResult
import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.newValue
import com.tobi.mc.noOptimisation
import com.tobi.mc.parser.util.copySource
import kotlin.reflect.KClass

class AssociativityReducer<T : Computable>(
    val type: KClass<T>,
    val createType: (Computable, Computable) -> T,
    val operation: (Long, Long) -> Long
) {

    /**
     * @return The new value, or null if no changes have been made
     */
    fun reduce(input: T): OptimisationResult<Computable> {
        val mergingList = MergingList()
        input.traverse(mergingList)

        if(!mergingList.hasModified) {
            return noOptimisation()
        }
        return newValue(mergingList.mergeResults())
    }

    private fun Computable.traverse(items: MergingList) {
        if(type.isInstance(this)) {
            for (node in this.getNodes()) {
                node.traverse(items)
            }
        } else {
            items.add(this)
        }
    }

    private inner class MergingList {
        val list = ArrayList<Computable>()
        var hasModified: Boolean = false

        fun add(computable: Computable) {
            if(computable is DataTypeInt) {
                if(!list.isEmpty() && list[0] is DataTypeInt) {
                    val first = list.first() as DataTypeInt
                    list[0] = DataTypeInt(operation(first.value, computable.value)).copySource(first, computable)
                    hasModified = true
                } else {
                    list.add(0, computable)
                }
            } else {
                list.add(computable)
            }
        }

        fun mergeResults(): Computable {
            if(list.isEmpty()) {
                throw IllegalStateException("List empty")
            }
            return list.reduce { c1, c2 ->
                createType(c1, c2).copySource(c1, c2)
            }
        }
    }
}