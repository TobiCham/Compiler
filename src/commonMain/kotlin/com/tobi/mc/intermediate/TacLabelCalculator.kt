package com.tobi.mc.intermediate

import com.tobi.mc.intermediate.code.TacBlock
import com.tobi.mc.intermediate.code.TacBranchEqualZero
import com.tobi.mc.intermediate.code.TacFunction
import com.tobi.mc.intermediate.code.TacGoto
import com.tobi.mc.intermediate.util.traverseAllNodes

object TacLabelCalculator {

    private class TacLabelsImpl : TacLabels {

        private var labels: Int = 0
        private val blockLabels: MutableMap<TacBlock, Int> = HashMap()

        override fun shouldGenerateLabel(block: TacBlock): Boolean {
            return blockLabels.containsKey(block)
        }

        override fun getLabel(block: TacBlock): String? {
            return formatLabel(blockLabels[block] ?: return null)
        }

        override fun generateNewLabel(): String {
            val result = formatLabel(this.labels)
            this.labels++
            return result
        }

        private fun formatLabel(index: Int): String {
            return "label${index}"
        }

        /**
         * @return The new number of connections
         */
        fun addConnection(toBlock: TacBlock, connections: MutableMap<TacBlock, Int>): Int {
            val existing = connections[toBlock] ?: 0
            val newTotal = existing + 1
            connections[toBlock] = newTotal
            if(newTotal > 1) {
                addBlockLabel(toBlock)
            }
            return newTotal
        }

        fun addBlockLabel(block: TacBlock) {
            if(!blockLabels.containsKey(block)) {
                blockLabels[block] = this.labels
                this.labels++
            }
        }
    }

    fun calculateLabels(tac: TacNode): TacLabels {
        val labels = TacLabelsImpl()
        val connections = HashMap<TacBlock, Int>()

        for (node in tac.traverseAllNodes()) {
            when(node) {
                is TacFunction -> labels.addConnection(node.code, connections)
                is TacGoto -> labels.addConnection(node.block, connections)
                is TacBranchEqualZero -> {
                    labels.addBlockLabel(node.successBlock)
                    labels.addConnection(node.failBlock, connections)
                }
            }
        }
        return labels
    }
}