package com.tobi.mc.parser

class Node(val type: NodeType, val left: Node, val right: Node, val lexeme: String, val value: Int) {

    val isLeaf
        get() = type == NodeType.LEAF
}