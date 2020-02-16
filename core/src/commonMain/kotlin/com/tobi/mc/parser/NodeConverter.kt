package com.tobi.mc.parser

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.util.LinkedStack
import com.tobi.util.MutableStack
import com.tobi.util.TypeNameConverter

//TODO Cleanup
object NodeConverter {

    fun convertProgram(node: Node): Program {
        val sequence = if(node.type == NodeType.FUNCTION_DECLARATION) {
            ExpressionSequence(listOf(convert(node)))
        } else {
            convertFunctionDeclarations(node)
        }
        return sequence.operations.map {
            it as? FunctionDeclaration ?: throw ParseException("Invalid top level type ${TypeNameConverter.getTypeName(this)}")
        }
    }

    private fun convertFunctionDeclarations(rootNode: Node): ExpressionSequence {
        val result = ArrayList<FunctionDeclaration>()
        val stack: MutableStack<Node> = LinkedStack()
        stack.push(rootNode)

        while(!stack.isEmpty()) {
            val node = stack.pop()
            when(node.type) {
                NodeType.FUNCTION_DECLARATION -> result.add(convertFunction(node))
                NodeType.TILDE -> {
                    stack.push(node.right!!)
                    stack.push(node.left!!)
                }
                else -> throw ParseException("Unknown node $node")
            }
        }
        return ExpressionSequence(result)
    }

    private fun convert(node: Node): Computable {
        if(node.isLeaf) return getLeaf(node.left!!)
        return when(node.type) {
            NodeType.RETURN -> ReturnExpression(if (node.left == null) null else convert(node.left))
            NodeType.BREAK -> BreakStatement
            NodeType.CONTINUE -> ContinueStatement
            NodeType.FUNCTION_DECLARATION -> convertFunction(node)
            NodeType.ADD -> convertArithmetic(node, ::Add)
            NodeType.MINUS -> convertArithmetic(node, ::Subtract)
            NodeType.MULTIPLY -> convertArithmetic(node, ::Multiply)
            NodeType.DIVIDE -> convertArithmetic(node, ::Divide)
            NodeType.MOD -> convertArithmetic(node, ::Mod)
            NodeType.GE_OP -> convertArithmetic(node, ::GreaterThanOrEqualTo)
            NodeType.GREATER_THAN -> convertArithmetic(node, ::GreaterThan)
            NodeType.LE_OP -> convertArithmetic(node, ::LessThanOrEqualTo)
            NodeType.LESS_THAN -> convertArithmetic(node, ::LessThan)
            NodeType.EQ_OP -> convertArithmetic(node, ::Equals)
            NodeType.NE_OP -> convertArithmetic(node, ::NotEquals)
            NodeType.APPLY -> convertFunctionCall(node)
            NodeType.TILDE -> convertVariableDefinition(node)
            NodeType.ASSIGNMENT -> convertVariableAssignment(node)
            NodeType.SEMI_COLON -> convertSequence(node)
            NodeType.IF -> convertIfStatement(node)
            NodeType.WHILE -> convertWhileLoop(node)
            else -> throw ParseException("Invalid node ${node.type}")
        }
    }

    private fun <T : Computable> convertArithmetic(node: Node, createExpression: (first: Computable, second: Computable) -> T): T {
        val left = node.left ?: throw ParseException("Missing left hand side for expression")
        val right = node.right ?: throw ParseException("Missing right hand side for expression")
        return createExpression(convert(left), convert(right))
    }

    private fun convertFunction(node: Node): FunctionDeclaration {
        val left = node.left
        val right = node.right
        val sequence = when {
            right == null -> ExpressionSequence(emptyList())
            right.type == NodeType.SEMI_COLON -> convertSequence(right)
            else -> ExpressionSequence(listOf(convert(right)))
        }
        val returnType = if(left.left == null) null else getDataType(left.left.left.lexeme)
        val functionName = left.right!!.left!!.left!!.lexeme
        val parameterList = getParameterList(left.right.right)

        return FunctionDeclaration(functionName, parameterList, sequence, returnType)
    }

    private fun convertFunctionCall(functionCallNode: Node): FunctionCall {
        val left = functionCallNode.left

        val getFunction = when {
            left!!.isLeaf -> GetVariable(left.left!!.lexeme)
            left.type == NodeType.APPLY -> convertFunctionCall(left)
            else -> throw ParseException("Unknown function application to $functionCallNode")
        }
        if(functionCallNode.right == null) {
            return FunctionCall(getFunction, emptyList())
        }

        val stack: MutableStack<Node> = LinkedStack()
        stack.push(functionCallNode.right)

        val args = ArrayList<DataComputable>()
        while(!stack.isEmpty()) {
            val node = stack.pop()
            if(node.type == NodeType.COMMA) {
                stack.push(node.right!!)
                stack.push(node.left!!)
            } else {
                val result = convert(node)
                args.add(result.asData("a function argument"))
            }
        }

        return FunctionCall(getFunction, args)
    }

    private fun getDataType(name: String) =
        DataType.values().find { it.toString().equals(name, true) } ?: throw ParseException("Unknown data type '$name'")

    private fun getParameterList(functionNode: Node?): ParameterList {
        if(functionNode == null) return emptyList()

        val params = ArrayList<Pair<String, DataType>>()
        val stack: MutableStack<Node> = LinkedStack()
        stack.push(functionNode)

        while(!stack.isEmpty()) {
            val node = stack.pop()
            when(node.type) {
                NodeType.COMMA -> {
                    stack.push(node.right!!)
                    stack.push(node.left!!)
                }
                NodeType.TILDE -> params.add(Pair(node.right!!.left!!.lexeme, getDataType(node.left!!.left!!.lexeme)))
                else -> throw ParseException("Unexpected node $node")
            }
        }
        return params
    }

    private fun convertSequence(sequenceNode: Node): ExpressionSequence {
        val expressions = ArrayList<Computable>()
        val stack: MutableStack<Node> = LinkedStack()
        stack.push(sequenceNode)

        while(!stack.isEmpty()) {
            val node = stack.pop()
            if(node.type == NodeType.SEMI_COLON) {
                stack.push(node.right!!)
                stack.push(node.left!!)
            } else {
                expressions.add(convert(node))
            }
        }
        return ExpressionSequence(expressions)
    }

    private fun convertVariableDefinition(node: Node): DefineVariable {
        val type = if(node.left == null) null else getDataType(node.left.left!!.lexeme)
        val assignment = if(!node.right!!.isLeaf) {
            convertVariableAssignment(node.right)
        } else {
            if(type == null) throw ParseException("Cannot have an auto variable with no assignment")
            SetVariable(
                node.right.left!!.lexeme, when (type) {
                    DataType.INT -> DataTypeInt(0)
                    DataType.STRING -> DataTypeString("")
                    else -> throw ParseException("No default type for $type")
                }
            )
        }

        return DefineVariable(assignment.name, assignment.value, type)
    }

    private fun convertVariableAssignment(node: Node): SetVariable {
        val name = node.left!!.left!!.lexeme
        val value = convert(node.right!!)
        return SetVariable(name, value.asData("a variable assignment"))
    }

    private fun convertIfStatement(node: Node): IfStatement {
        val check = convert(node.left!!).asData("a condition")

        if(node.right!!.type == NodeType.ELSE) {
            var ifSequence = convert(node.right.left!!)
            if(ifSequence !is ExpressionSequence) {
                ifSequence = ExpressionSequence(listOf(ifSequence))
            }
            var elseSequence = if(node.right.right == null) {
                ExpressionSequence(emptyList())
            } else {
                convert(node.right.right)
            }
            if(elseSequence !is ExpressionSequence) {
                elseSequence = ExpressionSequence(listOf(elseSequence))
            }
            return IfStatement(check, ifSequence, elseSequence)
        }
        var ifSequence = convert(node.right)
        if(ifSequence !is ExpressionSequence) {
            ifSequence = ExpressionSequence(listOf(ifSequence))
        }
        return IfStatement(check, ifSequence, null)
    }

    private fun convertWhileLoop(node: Node): WhileLoop {
        val check = convert(node.left!!)
        var body = convert(node.right!!)
        if(body !is ExpressionSequence) {
            body = ExpressionSequence(listOf(body))
        }
        return WhileLoop(check.asData("a condition"), body)
    }

    private fun getLeaf(node: Node): Computable {
        return when(node.type) {
            NodeType.CONSTANT -> DataTypeInt(node.value)
            NodeType.STRING -> DataTypeString(node.lexeme)
            NodeType.IDENTIFIER -> GetVariable(node.lexeme)
            else -> throw ParseException("Unexpected node ${node.type}")
        }
    }

    private fun Computable.asData(description: String): DataComputable {
        return this as? DataComputable ?: throw ParseException("Cannot use ${TypeNameConverter.getTypeName(this)} as $description")
    }
}