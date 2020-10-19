package monaco.languages

import monaco.IMarkdownString

data class SimpleCompletionItem(
    override var label: String,
    override var documentation: IMarkdownString,
    override var detail: String?,
    override var kind: Int,
    override var insertText: String,
) : CompletionItem