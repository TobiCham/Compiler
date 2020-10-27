package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.Computable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.variable.GetVariable
import com.tobi.mc.parser.util.copySource
import com.tobi.mc.parser.util.getComponents
import kotlin.reflect.KClass

class AssociativityReducer<T : Computable>(
    val type: KClass<T>,
    val createType: (Computable, Computable) -> T,
    val operation: (Long, Long) -> Long
) {

    /**
     * @return The new value, or null if no changes have been made
     */
    fun reduce(input: T): Computable? {
        val mergingList = MergingList()
        input.traverse(mergingList)

        if(!mergingList.hasModified) {
            return null
        }
        return mergingList.mergeResults()
    }

    private fun Computable.traverse(items: MergingList) {
        if(!type.isInstance(this)) {
            items.add(this)

            if(this !is DataTypeInt && this !is GetVariable) {
                return
            }
        }
        for (component in this.getComponents()) {
            component.traverse(items)
        }
    }

    private inner class MergingList {
        val list = ArrayList<Computable>()
        var hasModified: Boolean = false

        fun add(computable: Computable) {
            if(computable is DataTypeInt) {
                if(list.isEmpty()) list.add(computable)
                else {
                    val first = list.first()
                    if(first is DataTypeInt) {
                        list[0] = DataTypeInt(operation(first.value, computable.value)).copySource(first, computable)
                        hasModified = true
                    } else {
                        list.add(0, computable)
                    }
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