package com.tobi.mc.main

import com.tobi.mc.inbuilt.DefaultContext
import com.tobi.mc.inbuilt.InbuiltFunction
import monaco.CancellationToken
import monaco.IMarkdownString
import monaco.Position
import monaco.editor.ITextModel
import monaco.languages.*
import kotlin.js.RegExp

object MinusCLanguage : EditorLanguage {

    override val id: String = "minusc"

    override val configuration: LanguageConfiguration = object : LanguageConfiguration {
        override var autoClosingPairs: Array<IAutoClosingPairConditional>? = arrayOf(
            AutoClosingPair("{", "}"),
            AutoClosingPair("(", ")"),
            AutoClosingPair("\"", "\"")
        )

        override var brackets: Array<dynamic>? = arrayOf(
            arrayOf("{", "}"),
            arrayOf("(", ")")
        )

        override var comments: CommentRule? = object : CommentRule {
            override var lineComment: String? = "//"
            override var blockComment: dynamic = arrayOf("/*", "*/")
        }
    }

    override val language: IMonarchLanguage = object : IMonarchLanguage {

        private val KEYWORDS = arrayOf("continue", "break", "if", "else", "extern", "return", "while")
        private val TYPES = arrayOf("auto", "int", "string", "function", "void")
        private val OPERATORS = arrayOf("++", "<=", ">=", "==", "!=", ";", ",", "=", "!", "-", "+", "*", "/", "%", "<", ">")
        private val BRACKETS = arrayOf("{", "}", "(", ")")

        private val rules: MutableList<SimpleTokeniser.Rule> = ArrayList()
        init {
            rules.addAll(KEYWORDS.map { keyword ->
                SimpleTokeniser.Rule(RegExp(keyword.escapeRegex()), "keyword")
            })
            rules.addAll(TYPES.map { type ->
                SimpleTokeniser.Rule(RegExp(type.escapeRegex()), "keyword")
            })
            rules.addAll(OPERATORS.map { type ->
                SimpleTokeniser.Rule(RegExp(type.escapeRegex()), "operator")
            })
            rules.addAll(BRACKETS.map { type ->
                SimpleTokeniser.Rule(RegExp(type.escapeRegex()), "@brackets")
            })
            rules.add(SimpleTokeniser.Rule(RegExp("\"(\\.|[^\\\"])*?\""), "string"))
            rules.add(SimpleTokeniser.Rule(RegExp("-?[0-9]+"), "number"))

            rules.add(SimpleTokeniser.Rule(RegExp("(\\/\\/).*"), "comment"))
            rules.add(SimpleTokeniser.Rule(RegExp("\\/\\*(.|\\n)*?\\*\\/"), "comment"))
            rules.add(SimpleTokeniser.Rule(RegExp("([a-zA-Z_])([a-zA-Z_]|[0-9])+"), "identifier"))
            rules.add(SimpleTokeniser.Rule(RegExp("."), "else"))
        }

        override var brackets: Array<IMonarchLanguageBracket>? = arrayOf(
            MonarchLanguageBracket("{", "}", "@brackets"),
            MonarchLanguageBracket("(",")", "@brackets")
        )

        override var tokenizer: Any = SimpleTokeniser.from(
            SimpleTokeniser.Entry("root", *rules.toTypedArray())
        )
    }

    private fun createDescription(function: InbuiltFunction): String = buildString {
        append(getFunctionSignature(function))
    }

    private fun getFunctionSignature(function: InbuiltFunction): String = buildString {
        append(function.expandedType.returnType)
        append(' ')
        append(function.functionDescription.name)
        append('(')
        append(function.params.joinToString(separator = ", ") {
            "${it.second} ${it.first}"
        })
        append(')')
    }

    private fun createTextInsertion(function: InbuiltFunction): String = buildString {
        append(function.functionDescription.name)
        append('(')
        append(function.params.mapIndexed { index, pair ->
            "\${${index + 1}:${pair.first}}"
        }.joinToString(", "))
        append(')')
    }

    private val codeCompletionItems = DefaultContext().getVariables().filter { it.value is InbuiltFunction }.map {
        val func = it.value as InbuiltFunction
        SimpleCompletionItem(func.functionDescription.name, object : IMarkdownString {
            override var value: String = createDescription(func)
        }, func.functionDescription.description, CompletionItemKind.Function, createTextInsertion(func)).apply {
            if(range != null) {
                this.range = range
            }
            insertTextRules = CompletionItemInsertTextRule.InsertAsSnippet
        }
    }.toTypedArray()

    override val codeActions: CodeActionProvider? = null

    override val codeCompletions: CompletionItemProvider = object : CompletionItemProvider {
        override fun provideCompletionItems(
            model: ITextModel,
            position: Position,
            context: CompletionContext,
            token: CancellationToken
        ): dynamic {
            val suggestions = codeCompletionItems
            return jsObject {
                this.suggestions = suggestions
                this.dispose = js("function() {}")
            }
        }
    }
}