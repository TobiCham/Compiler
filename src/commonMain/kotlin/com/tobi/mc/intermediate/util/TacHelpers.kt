package com.tobi.mc.intermediate.util

import com.tobi.mc.intermediate.TacNode
import com.tobi.mc.intermediate.TacProgram
import com.tobi.mc.intermediate.code.*
import com.tobi.mc.util.ArrayListStack
import com.tobi.mc.util.MutableStack
import com.tobi.mc.util.typeName
import kotlin.reflect.KMutableProperty0

fun TacNode.updateNodeAtIndex(index: Int, node: TacNode?) {
    fun update(vararg properties: KMutableProperty0<out TacNode?>): Boolean {
        (properties[index] as KMutableProperty0<TacNode?>).set(node)
        if(index < 0 || index >= properties.size) {
            return false
        }
        return true
    }
    val updated = when(this) {
        is TacBlock -> {
            if(index < 0 || index >= this.instructions.size) {
                false
            }
            if(node == null) {
                this.instructions.removeAt(index)
            } else {
                this.instructions.set(index, node)
            }
            true
        }
        is TacBranchEqualZero -> update(this::conditionVariable, this::successBlock, this::failBlock)
        is TacSetVariable -> update(this::variable, this::value)
        is TacSetArgument -> update(this::variable)
        is TacGoto -> update(this::block)
        is TacFunctionCall -> update(this::function)
        is TacStringConcat -> update(this::str1, this::str2)
        is TacUnaryMinus -> update(this::variable)
        is TacNegation -> update(this::toNegate)
        is TacMathOperation -> update(this::arg1, this::arg2)
        is TacFunction -> update(this::code)
        is TacProgram -> update(this::mainFunction)
        else -> throw IllegalStateException("Can't update nodes for ${this.typeName}")
    }
    if(!updated) {
        throw IllegalStateException("Invalid node index $index for ${this.typeName}")
    }
}

fun TacNode.traverseAllNodes(filter: (node: TacNode) -> Boolean = { true }) = sequence {
    val uniqueBlocks = HashSet<TacBlock>()
    val stack: MutableStack<TacNode> = ArrayListStack()
    stack.push(this@traverseAllNodes)

    while(!stack.isEmpty()) {
        val element = stack.pop()
        if(element is TacBlock) {
            if(!uniqueBlocks.add(element)) continue
        }
        if(filter(element)) {
            yield(element)
            element.getNodes().forEach(stack::push)
        }
    }
}