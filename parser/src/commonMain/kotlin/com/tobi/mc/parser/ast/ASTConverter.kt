package com.tobi.mc.parser.ast

import com.tobi.mc.computable.*
import com.tobi.mc.computable.data.DataType
import com.tobi.mc.computable.data.DataTypeInt
import com.tobi.mc.computable.data.DataTypeString
import com.tobi.mc.parser.ast.parser.ParserNode
import com.tobi.mc.parser.ast.parser.ParserNodeType
import com.tobi.util.TypeNameConverter
import com.tobi.util.mapIfNotNull

internal object ASTConverter {

    fun convertProgram(node: ParserNode): Program {
        val sequence = if(node.type == ParserNodeType.FUNCTION) {
            ExpressionSequence(listOf(convert(node)))
        } else {
            convertFunctionDeclarations(node)
        }
        return sequence.operations.map {
            it as? FunctionDeclaration ?: throw makeException("Invalid top level type ${TypeNameConverter.getTypeName(this)}", node)
        }
    }

    private fun convertFunctionDeclarations(rootNode: ParserNode) = ExpressionSequence(recurse(rootNode) { node, result, recurse ->
        when(node.type) {
            ParserNodeType.FUNCTION_DEF -> result.add(convertFunction(node))
            ParserNodeType.TILDE -> {
                recurse(node.left!!)
                recurse(node.right!!)
            }
            else -> throw makeException("Unknown node ${node.type}", node)
        }
    })

    private fun convert(node: ParserNode): Computable  = when(node.type) {
        ParserNodeType.IDENTIFIER -> GetVariable(node.value as String)
        ParserNodeType.STRING -> DataTypeString(node.value as String)
        ParserNodeType.CONSTANT -> DataTypeInt(node.value as Int)
        ParserNodeType.RETURN -> ReturnExpression(if (node.left == null) null else convert(node.left))
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
        else -> throw makeException("Invalid node ${node.type}", node)
    }

    private fun <T : Computable> convertArithmetic(node: ParserNode, createExpression: (first: Computable, second: Computable) -> T): T {
        val left = node.left ?: throw makeException("Missing left hand side for expression", node)
        val right = node.right ?: throw makeException("Missing right hand side for expression", node)
        return createExpression(convert(left), convert(right))
    }

    private fun convertFunction(node: ParserNode): FunctionDeclaration {
        val left = node.left!!
        val right = node.right
        val sequence = when {
            right == null -> ExpressionSequence(emptyList())
            right.type == ParserNodeType.END_STATEMENT -> convertSequence(right)
            else -> ExpressionSequence(listOf(convert(right)))
        }
        val returnType = left.left?.mapIfNotNull(this::getDataType)
        val functionName = left.right!!.left!!.value as String
        val parameterList = left.right.right.mapIfNotNull(this::getParameterList) ?: emptyList()

        return FunctionDeclaration(functionName, parameterList, sequence, returnType)
    }

    private fun convertFunctionCall(functionCallNode: ParserNode): FunctionCall {
        val left = functionCallNode.left

        val getFunction = when(left!!.type) {
            ParserNodeType.APPLY -> convertFunctionCall(left)
            ParserNodeType.IDENTIFIER -> GetVariable(left.value as String)
            else -> throw makeException("Unknown function application to ${functionCallNode.type}", functionCallNode)
        }
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

                result.add(name to type)
            }
            else -> throw makeException("Unexpected node ${node.type}", node)
        }
    }

    private fun convertSequence(sequenceNode: ParserNode) = ExpressionSequence(recurse(sequenceNode) { node, result, recurse ->
        if(node.type == ParserNodeType.END_STATEMENT) {
            recurse(node.left!!)
            recurse(node.right!!)
        } else {
            result.add(convert(node))
        }
    })

    private fun convertVariableDefinition(node: ParserNode): DefineVariable {
        val type = if(node.left == null) null else getDataType(node.left)
        if(type == DataType.VOID) {
            throw makeException("Cannot declare a variable as void", if(node.right!!.type == ParserNodeType.ASSIGNMENT) node.right.left!! else node.right)
        }

        val assignment = if(node.right!!.type == ParserNodeType.ASSIGNMENT) {
            convertVariableAssignment(node.right)
        } else {
            if(type == null) throw makeException("Cannot have an auto variable with no assignment", node.right)
            SetVariable(node.right.value as String, when (type) {
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
        return SetVariable(name, value.asData("a variable assignment", node.right))
    }

    private fun convertIfStatement(node: ParserNode): IfStatement {
        val check = convert(node.left!!).asData("a condition", node.left)
        if(node.right!!.type == ParserNodeType.ELSE) {
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

    private fun convertWhileLoop(node: ParserNode): WhileLoop {
        val check = convert(node.left!!)
        var body = convert(node.right!!)
        if(body !is ExpressionSequence) {
            body = ExpressionSequence(listOf(body))
        }
        return WhileLoop(check.asData("a condition", node.left), body)
    }

    private fun Computable.asData(description: String, node: ParserNode): DataComputable {
        return this as? DataComputable ?: throw makeException(
            "Cannot use ${TypeNameConverter.getTypeName(
                this
            )} as $description", node
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