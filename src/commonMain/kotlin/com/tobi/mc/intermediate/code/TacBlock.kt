package com.tobi.mc.intermediate.code

import com.tobi.mc.intermediate.TacNode

class TacBlock(var instructions: MutableList<TacNode>) : TacNode {

    private val hash = hashCounter++

    override fun getNodes(): Iterable<TacNode> = ArrayList(this.instructions)

    override fun toString(): String = "Block(${instructions.size})"

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int = hash

    companion object {
        private var hashCounter = 1
    }
}