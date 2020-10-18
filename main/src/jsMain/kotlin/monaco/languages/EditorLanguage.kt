package monaco.languages

interface EditorLanguage {

    val id: String

    val configuration: LanguageConfiguration

    val language: IMonarchLanguage

    val codeActions: CodeActionProvider?

    val codeCompletions: CompletionItemProvider?
}

fun MonacoLanguages.register(language: EditorLanguage) {
    this.register(object : ILanguageExtensionPoint {
        override var id: String = language.id
    })
    this.setLanguageConfiguration(language.id, language.configuration)
    this.setMonarchTokensProvider(language.id, language.language)

    if(language.codeActions != null) {
        this.registerCodeActionProvider(language.id, language.codeActions!!)
    }
    if(language.codeCompletions != null) {
        this.registerCompletionItemProvider(language.id, language.codeCompletions!!)
    }
}