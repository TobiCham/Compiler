package com.tobi.mc.parser.optimisation.optimisations.number

import com.tobi.mc.computable.DataComputable
import com.tobi.mc.computable.GetVariable
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.parser.util.getComponents
import kotlin.reflect.KClass

internal class AssociativityReducer<T : DataComputable>(val type: KClass<T>, val createType: (DataComputable, DataComputable) -> T, val operation: (Long, Long) -> Long) {

    fun reduce(input: T): DataComputable {
        val mergingList = MergingList()
        input.traverse(mergingList)

        if(!mergingList.hasModified) {
            return input
        }
        return mergingList.mergeResults()
    }

    private fun DataComputable.traverse(items: MergingList) {
        if(!type.isInstance(this)) {
            items.add(this)

            if(this !is DataTypeInt && this !is GetVariable) {
                return
            }
        }
        for (component in this.getComponents()) {
            (component as DataComputable).traverse(items)
        }
    }

    private inner class MergingList {
        val list = ArrayList<DataComputable>()
        var hasModified: Boolean = false

        fun add(computable: DataComputable) {
            if(computable is DataTypeInt) {
                if(list.isEmpty()) list.add(computable)
                else {
                    val first = list.first()
                    if(first is DataTypeInt) {
                        list[0] = DataTypeInt(operation(first.value, computable.value))
                        hasModified = true
                    } else list.add(0, computable)
                }
            } else {
                list.add(computable)
            }
        }

        fun mergeResults(): DataComputable {
            if(list.isEmpty()) {
                throw IllegalStateException("List empty")
            }
            return list.reduce(createType)
        }
    }
}