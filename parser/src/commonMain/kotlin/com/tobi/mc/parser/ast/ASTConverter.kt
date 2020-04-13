package com.tobi.mc.parser.ast

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.ast.parser.ParserNode
import com.tobi.mc.parser.ast.parser.ParserNodeType
import com.tobi.mc.util.mapIfNotNull
import com.tobi.mc.util.typeName

internal object ASTConverter {

    fun convert(node: ParserNode): Computable  = when(node.type) {
        ParserNodeType.IDENTIFIER -> GetVariable(node.value as String, -1)
        ParserNodeType.STRING -> DataTypeString(node.value as String)
        ParserNodeType.CONSTANT -> DataTypeInt(node.value as Long)
        ParserNodeType.RETURN -> ReturnExpression(
            if (node.left == null) null else convert(node.left).asData("return", node.left)
        )
        ParserNodeType.BREAK -> BreakStatement
        ParserNodeType.CONTINUE -> ContinueStatement
        ParserNodeType.FUNCTION_DEF -> convertFunction(node)
        ParserNodeType.ADD -> convertArithmetic(node, ::Add)
        ParserNodeType.SUBTRACT -> convertArithmetic(node, ::Subtract)
        ParserNodeType.MULTIPLY -> convertArithmetic(node, ::Multiply)
        ParserNodeType.DIVIDE -> convertArithmetic(node, ::Divide)
        ParserNodeType.MOD -> convertArithmetic(node, ::Mod)
        ParserNodeType.MORE_EQ_TO -> convertArithmetic(node, ::GreaterThanOrEqualTo)
        ParserNodeType.MORE_THAN -> convertArithmetic(node, ::GreaterThan)
        ParserNodeType.LESS_EQ_TO -> convertArithmetic(node, ::LessThanOrEqualTo)
        ParserNodeType.LESS_THAN -> convertArithmetic(node, ::LessThan)
        ParserNodeType.EQUALS -> convertArithmetic(node, ::Equals)
        ParserNodeType.NOT_EQUALS -> convertArithmetic(node, ::NotEquals)
        ParserNodeType.APPLY -> convertFunctionCall(node)
        ParserNodeType.TILDE -> convertVariableDefinition(node)
        ParserNodeType.ASSIGNMENT -> convertVariableAssignment(node)
        ParserNodeType.END_STATEMENT -> convertSequence(node)
        ParserNodeType.IF -> convertIfStatement(node)
        ParserNodeType.WHILE -> convertWhileLoop(node)
        ParserNodeType.INT -> convertUnaryExpression(node)
        else -> throw makeException("Invalid node ${node.type}", node)
    }

    private fun <T : Computable> convertArithmetic(node: ParserNode, createExpression: (first: DataComputable, second: DataComputable) -> T): T {
        val left = node.left ?: throw makeException("Missing left hand side for expression", node)
        val right = node.right ?: throw makeException("Missing right hand side for expression", node)
        return createExpression(
            convert(left).asData("math operation", left),
            convert(right).asData("math operation", right)
        )
    }

    private fun convertUnaryExpression(node: ParserNode): DataComputable {
        node.left!!
        if(node.left.type == ParserNodeType.MULTIPLY) {
            //TODO Remove once fixed parser
            throw makeException("Pointers are not currently supported", node)
        }
        if(node.left.type == ParserNodeType.NOT) {
            return Negation(convert(node.right!!).asData("negation", node.right))
        }
        if(node.left.type == ParserNodeType.SUBTRACT || node.left.type == ParserNodeType.ADD) {
            val constant = convert(node.right!!)
            if(constant !is DataTypeInt) {
                throw makeException("Expected constant, got ${node.right.type.toString().toLowerCase()}", node.right)
            }
            if(node.left.type == ParserNodeType.ADD) {
                return constant
            }
            return DataTypeInt(constant.value * -1L)
        }
        throw makeException("Can't parse expression", node)
    }

    private fun convertFunction(node: ParserNode): FunctionDeclaration {
        val left = node.left!!
        val sequence = convertSequence(node.right)
        val returnType = left.left?.mapIfNotNull(this::getDataType)
        val functionName = left.right!!.left!!.value as String
        val parameterList = left.right.right.mapIfNotNull(this::getParameterList) ?: emptyList()

        return FunctionDeclaration(functionName, parameterList, sequence, returnType)
    }

    private fun convertFunctionCall(functionCallNode: ParserNode): FunctionCall {
        val left = functionCallNode.left
        if(left!!.type != ParserNodeType.APPLY && left.type != ParserNodeType.IDENTIFIER) {
            throw makeException("Unknown function application to ${functionCallNode.type}", functionCallNode)
        }

        val getFunction = convert(left).asData("function", left)
        if(functionCallNode.right == null) {
            return FunctionCall(getFunction, emptyList())
        }

        val args: List<DataComputable> = recurse(functionCallNode.right) { node, result, recurse ->
            if(node.type == ParserNodeType.PARAMS_SEPARATOR) {
                recurse(node.left!!)
                recurse(node.right!!)
            } else {
                result.add(convert(node).asData("a function argument", node))
            }
        }
        return FunctionCall(getFunction, args)
    }

    private fun getDataType(node: ParserNode) = when(node.type) {
        ParserNodeType.AUTO -> null
        ParserNodeType.INT -> DataType.INT
        ParserNodeType.STRING -> DataType.STRING
        ParserNodeType.FUNCTION -> DataType.FUNCTION
        ParserNodeType.VOID -> DataType.VOID
        else -> throw makeException("Invalid data type ${node.type}", node)
    }

    private fun getParameterList(functionNode: ParserNode): ParameterList = recurse(functionNode) { node, result, recurse ->
        when(node.type) {
            ParserNodeType.PARAMS_SEPARATOR -> {
                recurse(node.left!!)
                recurse(node.right!!)
            }
            ParserNodeType.TILDE -> {
                val name = node.right!!.value as String
                val type = getDataType(node.left!!)
                if(type == null) throw makeException("Parameter cannot be auto", node)
                if(type == DataType.VOID) throw makeException("Parameter cannot be void", node)

                result.add(Parameter(type, name))
            }
            else -> throw makeException("Unexpected node ${node.type}", node)
        }
    }

    private fun convertSequence(sequenceNode: ParserNode?): ExpressionSequence {
        if(sequenceNode == null) return ExpressionSequence(emptyList())
        return ExpressionSequence(recurse(sequenceNode) { node, result, recurse ->
            if(node.type == ParserNodeType.END_STATEMENT) {
                recurse(node.left!!)
                recurse(node.right!!)
            } else {
                result.add(convert(node))
            }
        })
    }

    private fun convertVariableDefinition(node: ParserNode): DefineVariable {
        val type = if(node.left == null) null else getDataType(node.left)
        if(type == DataType.VOID) {
            throw makeException("Cannot declare a variable as void", if(node.right!!.type == ParserNodeType.ASSIGNMENT) node.right.left!! else node.right)
        }
        if(node.right?.type == ParserNodeType.FUNCTION_PARAMS) {
            throw makeException("Function declarations without an implementation are not supported", node)
        }

         val assignment = if(node.right!!.type == ParserNodeType.ASSIGNMENT) {
            convertVariableAssignment(node.right)
        } else {
            if(type == null) throw makeException("Cannot have an auto variable with no assignment", node.right)
            if(node.right.value == null) {
                println("aa")
            }
            SetVariable(node.right.value as String, -1, when (type) {
                DataType.INT -> DataTypeInt(0)
                DataType.STRING -> DataTypeString("")
                else -> throw makeException("No default type for $type", node.right)
            })
        }

        return DefineVariable(assignment.name, assignment.value, type)
    }

    private fun convertVariableAssignment(node: ParserNode): SetVariable {
        val name = node.left!!.value as String
        val value = convert(node.right!!)
        return SetVariable(name, -1, value.asData("a variable assignment", node.right))
    }

    private fun convertIfStatement(node: ParserNode): IfStatement {
        val check = convert(node.left!!).asData("a condition", node.left)
        if(node.right == null) {
            return IfStatement(check, convertSequence(null), null)
        }

        if(node.right.type == ParserNodeType.ELSE) {
            val ifSequence = convertSequence(node.right.left)
            val elseSequence = convertSequence(node.right.right)
            return IfStatement(check, ifSequence, elseSequence)
        }
        val ifSequence = convertSequence(node.right)
        return IfStatement(check, ifSequence, null)
    }

    private fun convertWhileLoop(node: ParserNode): WhileLoop {
        val check = convert(node.left!!)
        val body = convertSequence(node.right)
        return WhileLoop(check.asData("a condition", node.left), body)
    }

    private fun Computable.asData(description: String, node: ParserNode): DataComputable {
        return this as? DataComputable ?: throw makeException(
            "Cannot use ${this.typeName} as $description", node
        )
    }

    private fun <T> recurse(node: ParserNode, action: (ParserNode, MutableList<T>, (ParserNode) -> Unit) -> Unit): List<T> {
        val result = ArrayList<T>()

        fun doRecurse(node: ParserNode) {
            action(node, result, ::doRecurse)
        }
        doRecurse(node)
        return result
    }

    private fun makeException(message: String, node: ParserNode) =
        com.tobi.mc.parser.ParseException(message, node.leftLocation, node.rightLocation)
}