package monaco.languages

import monaco.*
import monaco.editor.EndOfLineSequence
import monaco.editor.IMarkerData
import monaco.editor.ISingleEditOperation
import monaco.editor.ITextModel
import org.khronos.webgl.Uint32Array
import org.w3c.dom.Range
import kotlin.js.RegExp

external interface MonacoLanguages {
    
    fun register(language: ILanguageExtensionPoint)

    fun getLanguages(): Array<ILanguageExtensionPoint>

    fun getEncodedLanguageId(languageId: String): Number

    fun onLanguage(languageId: String, callback: () -> Unit): IDisposable

    fun setLanguageConfiguration(languageId: String, configuration: LanguageConfiguration): IDisposable

    fun setTokensProvider(languageId: String, provider: TokensProvider): IDisposable

    fun setTokensProvider(languageId: String, provider: EncodedTokensProvider): IDisposable

    fun setTokensProvider(languageId: String, provider: Thenable<dynamic /* TokensProvider | EncodedTokensProvider */>): IDisposable

    fun setMonarchTokensProvider(languageId: String, languageDef: IMonarchLanguage): IDisposable

    fun setMonarchTokensProvider(languageId: String, languageDef: Thenable<IMonarchLanguage>): IDisposable

    fun registerReferenceProvider(languageId: String, provider: ReferenceProvider): IDisposable

    fun registerRenameProvider(languageId: String, provider: RenameProvider): IDisposable

    fun registerSignatureHelpProvider(languageId: String, provider: SignatureHelpProvider): IDisposable

    fun registerHoverProvider(languageId: String, provider: HoverProvider): IDisposable

    fun registerDocumentSymbolProvider(languageId: String, provider: DocumentSymbolProvider): IDisposable

    fun registerDocumentHighlightProvider(languageId: String, provider: DocumentHighlightProvider): IDisposable

    fun registerOnTypeRenameProvider(languageId: String, provider: OnTypeRenameProvider): IDisposable

    fun registerDefinitionProvider(languageId: String, provider: DefinitionProvider): IDisposable

    fun registerImplementationProvider(languageId: String, provider: ImplementationProvider): IDisposable

    fun registerTypeDefinitionProvider(languageId: String, provider: TypeDefinitionProvider): IDisposable

    fun registerCodeLensProvider(languageId: String, provider: CodeLensProvider): IDisposable

    fun registerCodeActionProvider(languageId: String, provider: CodeActionProvider): IDisposable

    fun registerDocumentFormattingEditProvider(languageId: String, provider: DocumentFormattingEditProvider): IDisposable

    fun registerDocumentRangeFormattingEditProvider(languageId: String, provider: DocumentRangeFormattingEditProvider): IDisposable

    fun registerOnTypeFormattingEditProvider(languageId: String, provider: OnTypeFormattingEditProvider): IDisposable

    fun registerLinkProvider(languageId: String, provider: LinkProvider): IDisposable

    fun registerCompletionItemProvider(languageId: String, provider: CompletionItemProvider): IDisposable

    fun registerColorProvider(languageId: String, provider: DocumentColorProvider): IDisposable

    fun registerFoldingRangeProvider(languageId: String, provider: FoldingRangeProvider): IDisposable

    fun registerDeclarationProvider(languageId: String, provider: DeclarationProvider): IDisposable

    fun registerSelectionRangeProvider(languageId: String, provider: SelectionRangeProvider): IDisposable

    fun registerDocumentSemanticTokensProvider(languageId: String, provider: DocumentSemanticTokensProvider): IDisposable

    fun registerDocumentRangeSemanticTokensProvider(languageId: String, provider: DocumentRangeSemanticTokensProvider): IDisposable
}

external interface IToken {
    var startIndex: Number
    var scopes: String
}

external interface ILineTokens {
    var tokens: Array<IToken>
    var endState: IState
}

external interface IEncodedLineTokens {
    var tokens: Uint32Array
    var endState: IState
}

external interface TokensProvider {
    fun getInitialState(): IState
    fun tokenize(line: String, state: IState): ILineTokens
}

external interface EncodedTokensProvider {
    fun getInitialState(): IState
    fun tokenizeEncoded(line: String, state: IState): IEncodedLineTokens
}

external interface CodeActionContext {
    var markers: Array<IMarkerData>
    var only: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CodeActionProvider {
    fun provideCodeActions(model: ITextModel, range: monaco.Range, context: CodeActionContext, token: CancellationToken): dynamic /* CodeActionList? | Thenable<CodeActionList?>? */
}

external interface CommentRule {
    var lineComment: String?
        get() = definedExternally
        set(value) = definedExternally
    var blockComment: dynamic /* JsTuple<String, String> */
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$9` {
    var docComment: IDocComment?
        get() = definedExternally
        set(value) = definedExternally
}

external interface LanguageConfiguration {
    var comments: CommentRule?
        get() = definedExternally
        set(value) = definedExternally
    var brackets: Array<dynamic /* JsTuple<String, String> */>?
        get() = definedExternally
        set(value) = definedExternally
    var wordPattern: RegExp?
        get() = definedExternally
        set(value) = definedExternally
    var indentationRules: IndentationRule?
        get() = definedExternally
        set(value) = definedExternally
    var onEnterRules: Array<OnEnterRule>?
        get() = definedExternally
        set(value) = definedExternally
    var autoClosingPairs: Array<IAutoClosingPairConditional>?
        get() = definedExternally
        set(value) = definedExternally
    var surroundingPairs: Array<IAutoClosingPair>?
        get() = definedExternally
        set(value) = definedExternally
    var autoCloseBefore: String?
        get() = definedExternally
        set(value) = definedExternally
    var folding: FoldingRules?
        get() = definedExternally
        set(value) = definedExternally
    var __electricCharacterSupport: `T$9`?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IndentationRule {
    var decreaseIndentPattern: RegExp
    var increaseIndentPattern: RegExp
    var indentNextLinePattern: RegExp?
        get() = definedExternally
        set(value) = definedExternally
    var unIndentedLinePattern: RegExp?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FoldingMarkers {
    var start: RegExp
    var end: RegExp
}

external interface FoldingRules {
    var offSide: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var markers: FoldingMarkers?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OnEnterRule {
    var beforeText: RegExp
    var afterText: RegExp?
        get() = definedExternally
        set(value) = definedExternally
    var oneLineAboveText: RegExp?
        get() = definedExternally
        set(value) = definedExternally
    var action: EnterAction
}

external interface IDocComment {
    var open: String
    var close: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IAutoClosingPair {
    var open: String
    var close: String
}

external interface IAutoClosingPairConditional : IAutoClosingPair {
    var notIn: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class IndentAction {
    None /* = 0 */,
    Indent /* = 1 */,
    IndentOutdent /* = 2 */,
    Outdent /* = 3 */
}

external interface EnterAction {
    var indentAction: IndentAction
    var appendText: String?
        get() = definedExternally
        set(value) = definedExternally
    var removeText: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IState {
    fun clone(): IState
    fun equals(other: IState): Boolean
}

external interface Hover {
    var contents: Array<IMarkdownString>
    var range: IRange?
        get() = definedExternally
        set(value) = definedExternally
}

external interface HoverProvider {
    fun provideHover(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Hover? | Thenable<Hover?>? */
}

object CompletionItemKind {
    const val Method = 0
    const val Function = 1
    const val Constructor = 2
    const val Field = 3
    const val Variable = 4
    const val Class = 5
    const val Struct = 6
    const val Interface = 7
    const val Module = 8
    const val Property = 9
    const val Event = 10
    const val Operator = 11
    const val Unit = 12
    const val Value = 13
    const val Constant = 14
    const val Enum = 15
    const val EnumMember = 16
    const val Keyword = 17
    const val Text = 18
    const val Color = 19
    const val File = 20
    const val Reference = 21
    const val Customcolor = 22
    const val Folder = 23
    const val TypeParameter = 24
    const val Snippet = 25
}

external interface CompletionItemLabel {
    var name: String
    var parameters: String?
        get() = definedExternally
        set(value) = definedExternally
    var qualifier: String?
        get() = definedExternally
        set(value) = definedExternally
    var type: String?
        get() = definedExternally
        set(value) = definedExternally
}

object CompletionItemTag {
    const val Deprecated = 1
}

object CompletionItemInsertTextRule {
    const val KeepWhitespace = 1
    const val InsertAsSnippet = 4
}

external interface `T$10` {
    var insert: IRange
    var replace: IRange
}

external interface CompletionItem {
    var label: dynamic /* String | CompletionItemLabel */
        get() = definedExternally
        set(value) = definedExternally
    var kind: Int
    var tags: Array<Int>?
        get() = definedExternally
        set(value) = definedExternally
    var detail: String?
        get() = definedExternally
        set(value) = definedExternally
    var documentation: dynamic /* String? | IMarkdownString? */
        get() = definedExternally
        set(value) = definedExternally
    var sortText: String?
        get() = definedExternally
        set(value) = definedExternally
    var filterText: String?
        get() = definedExternally
        set(value) = definedExternally
    var preselect: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var insertText: String
    var insertTextRules: Int?
        get() = definedExternally
        set(value) = definedExternally
    var range: dynamic /* IRange | `T$10` */
        get() = definedExternally
        set(value) = definedExternally
    var commitCharacters: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var additionalTextEdits: Array<ISingleEditOperation>?
        get() = definedExternally
        set(value) = definedExternally
    var command: Command?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CompletionList {
    var suggestions: Array<CompletionItem>
    var incomplete: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    val dispose: (() -> Unit)?
        get() = definedExternally
}

external enum class CompletionTriggerKind {
    Invoke /* = 0 */,
    TriggerCharacter /* = 1 */,
    TriggerForIncompleteCompletions /* = 2 */
}

external interface CompletionContext {
    var triggerKind: CompletionTriggerKind
    var triggerCharacter: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CompletionItemProvider {
    var triggerCharacters: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    fun provideCompletionItems(model: ITextModel, position: Position, context: CompletionContext, token: CancellationToken): dynamic /* CompletionList? | Thenable<CompletionList?>? */
    val resolveCompletionItem: ((item: CompletionItem, token: CancellationToken) -> dynamic)?
        get() = definedExternally
}

external interface CodeAction {
    var title: String
    var command: Command?
        get() = definedExternally
        set(value) = definedExternally
    var edit: WorkspaceEdit?
        get() = definedExternally
        set(value) = definedExternally
    var diagnostics: Array<IMarkerData>?
        get() = definedExternally
        set(value) = definedExternally
    var kind: String?
        get() = definedExternally
        set(value) = definedExternally
    var isPreferred: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disabled: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CodeActionList : IDisposable {
    var actions: Array<CodeAction>
}

external interface ParameterInformation {
    var label: dynamic /* String | dynamic */
        get() = definedExternally
        set(value) = definedExternally
    var documentation: dynamic /* String? | IMarkdownString? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface SignatureInformation {
    var label: String
    var documentation: dynamic /* String? | IMarkdownString? */
        get() = definedExternally
        set(value) = definedExternally
    var parameters: Array<ParameterInformation>
    var activeParameter: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SignatureHelp {
    var signatures: Array<SignatureInformation>
    var activeSignature: Number
    var activeParameter: Number
}

external interface SignatureHelpResult : IDisposable {
    var value: SignatureHelp
}

external enum class SignatureHelpTriggerKind {
    Invoke /* = 1 */,
    TriggerCharacter /* = 2 */,
    ContentChange /* = 3 */
}

external interface SignatureHelpContext {
    var triggerKind: SignatureHelpTriggerKind
    var triggerCharacter: String?
        get() = definedExternally
        set(value) = definedExternally
    var isRetrigger: Boolean
    var activeSignatureHelp: SignatureHelp?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SignatureHelpProvider {
    var signatureHelpTriggerCharacters: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var signatureHelpRetriggerCharacters: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    fun provideSignatureHelp(model: ITextModel, position: Position, token: CancellationToken, context: SignatureHelpContext): dynamic /* SignatureHelpResult? | Thenable<SignatureHelpResult?>? */
}

external enum class DocumentHighlightKind {
    Text /* = 0 */,
    Read /* = 1 */,
    Write /* = 2 */
}

external interface DocumentHighlight {
    var range: IRange
    var kind: DocumentHighlightKind?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DocumentHighlightProvider {
    fun provideDocumentHighlights(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Array<DocumentHighlight>? | Thenable<Array<DocumentHighlight>?>? */
}

external interface `T$11` {
    var ranges: Array<IRange>
    var wordPattern: RegExp?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OnTypeRenameProvider {
    var wordPattern: RegExp?
        get() = definedExternally
        set(value) = definedExternally
    fun provideOnTypeRenameRanges(model: ITextModel, position: Position, token: CancellationToken): dynamic /* `T$11`? | Thenable<`T$11`?>? */
}

external interface ReferenceContext {
    var includeDeclaration: Boolean
}

external interface ReferenceProvider {
    fun provideReferences(model: ITextModel, position: Position, context: ReferenceContext, token: CancellationToken): dynamic /* Array<Location>? | Thenable<Array<Location>?>? */
}

external interface Location {
    var uri: Uri
    var range: IRange
}

external interface LocationLink {
    var originSelectionRange: IRange?
        get() = definedExternally
        set(value) = definedExternally
    var uri: Uri
    var range: IRange
    var targetSelectionRange: IRange?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DefinitionProvider {
    fun provideDefinition(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Location? | Array<Location>? | Array<LocationLink>? | Thenable<dynamic /* Location? | Array<Location>? | Array<LocationLink>? */>? */
}

external interface DeclarationProvider {
    fun provideDeclaration(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Location? | Array<Location>? | Array<LocationLink>? | Thenable<dynamic /* Location? | Array<Location>? | Array<LocationLink>? */>? */
}

external interface ImplementationProvider {
    fun provideImplementation(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Location? | Array<Location>? | Array<LocationLink>? | Thenable<dynamic /* Location? | Array<Location>? | Array<LocationLink>? */>? */
}

external interface TypeDefinitionProvider {
    fun provideTypeDefinition(model: ITextModel, position: Position, token: CancellationToken): dynamic /* Location? | Array<Location>? | Array<LocationLink>? | Thenable<dynamic /* Location? | Array<Location>? | Array<LocationLink>? */>? */
}

external enum class SymbolKind {
    File /* = 0 */,
    Module /* = 1 */,
    Namespace /* = 2 */,
    Package /* = 3 */,
    Class /* = 4 */,
    Method /* = 5 */,
    Property /* = 6 */,
    Field /* = 7 */,
    Constructor /* = 8 */,
    Enum /* = 9 */,
    Interface /* = 10 */,
    Function /* = 11 */,
    Variable /* = 12 */,
    Constant /* = 13 */,
    String /* = 14 */,
    Number /* = 15 */,
    Boolean /* = 16 */,
    Array /* = 17 */,
    Object /* = 18 */,
    Key /* = 19 */,
    Null /* = 20 */,
    EnumMember /* = 21 */,
    Struct /* = 22 */,
    Event /* = 23 */,
    Operator /* = 24 */,
    TypeParameter /* = 25 */
}

external enum class SymbolTag {
    Deprecated /* = 1 */
}

external interface DocumentSymbol {
    var name: String
    var detail: String
    var kind: SymbolKind
    var tags: Array<SymbolTag>
    var containerName: String?
        get() = definedExternally
        set(value) = definedExternally
    var range: IRange
    var selectionRange: IRange
    var children: Array<DocumentSymbol>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface DocumentSymbolProvider {
    var displayName: String?
        get() = definedExternally
        set(value) = definedExternally
    fun provideDocumentSymbols(model: ITextModel, token: CancellationToken): dynamic /* Array<DocumentSymbol>? | Thenable<Array<DocumentSymbol>?>? */
}

external interface TextEdit {
    var range: IRange
    var text: String
    var eol: EndOfLineSequence?
        get() = definedExternally
        set(value) = definedExternally
}

external interface FormattingOptions {
    var tabSize: Number
    var insertSpaces: Boolean
}

external interface DocumentFormattingEditProvider {
    var displayName: String?
        get() = definedExternally
        set(value) = definedExternally
    fun provideDocumentFormattingEdits(model: ITextModel, options: FormattingOptions, token: CancellationToken): dynamic /* Array<TextEdit>? | Thenable<Array<TextEdit>?>? */
}

external interface DocumentRangeFormattingEditProvider {
    var displayName: String?
        get() = definedExternally
        set(value) = definedExternally
    fun provideDocumentRangeFormattingEdits(model: ITextModel, range: Range, options: FormattingOptions, token: CancellationToken): dynamic /* Array<TextEdit>? | Thenable<Array<TextEdit>?>? */
}

external interface OnTypeFormattingEditProvider {
    var autoFormatTriggerCharacters: Array<String>
    fun provideOnTypeFormattingEdits(model: ITextModel, position: Position, ch: String, options: FormattingOptions, token: CancellationToken): dynamic /* Array<TextEdit>? | Thenable<Array<TextEdit>?>? */
}

external interface ILink {
    var range: IRange
    var url: dynamic /* Uri? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var tooltip: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ILinksList {
    var links: Array<ILink>
    val dispose: (() -> Unit)?
        get() = definedExternally
}

external interface LinkProvider {
    fun provideLinks(model: ITextModel, token: CancellationToken): dynamic /* ILinksList? | Thenable<ILinksList?>? */
    var resolveLink: ((link: ILink, token: CancellationToken) -> dynamic)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IColor {
    var red: Number
    var green: Number
    var blue: Number
    var alpha: Number
}

external interface IColorPresentation {
    var label: String
    var textEdit: TextEdit?
        get() = definedExternally
        set(value) = definedExternally
    var additionalTextEdits: Array<TextEdit>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IColorInformation {
    var range: IRange
    var color: IColor
}

external interface DocumentColorProvider {
    fun provideDocumentColors(model: ITextModel, token: CancellationToken): dynamic /* Array<IColorInformation>? | Thenable<Array<IColorInformation>?>? */
    fun provideColorPresentations(model: ITextModel, colorInfo: IColorInformation, token: CancellationToken): dynamic /* Array<IColorPresentation>? | Thenable<Array<IColorPresentation>?>? */
}

external interface SelectionRange {
    var range: IRange
}

external interface SelectionRangeProvider {
    fun provideSelectionRanges(model: ITextModel, positions: Array<Position>, token: CancellationToken): dynamic /* Array<Array<SelectionRange>>? | Thenable<Array<Array<SelectionRange>>?>? */
}

external interface FoldingContext

external interface FoldingRangeProvider {
    fun provideFoldingRanges(model: ITextModel, context: FoldingContext, token: CancellationToken): dynamic /* Array<FoldingRange>? | Thenable<Array<FoldingRange>?>? */
}

external interface FoldingRange {
    var start: Number
    var end: Number
    var kind: FoldingRangeKind?
        get() = definedExternally
        set(value) = definedExternally
}

external open class FoldingRangeKind(value: String) {
    open var value: String

    companion object {
        var Comment: FoldingRangeKind
        var Imports: FoldingRangeKind
        var Region: FoldingRangeKind
    }
}

external interface `T$12` {
    var id: String
}

external interface `T$13` {
    var light: Uri
    var dark: Uri
}

external interface WorkspaceEditMetadata {
    var needsConfirmation: Boolean
    var label: String
    var description: String?
        get() = definedExternally
        set(value) = definedExternally
    var iconPath: dynamic /* `T$12`? | Uri? | `T$13`? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface WorkspaceFileEditOptions {
    var overwrite: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreIfNotExists: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreIfExists: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var recursive: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface WorkspaceFileEdit {
    var oldUri: Uri?
        get() = definedExternally
        set(value) = definedExternally
    var newUri: Uri?
        get() = definedExternally
        set(value) = definedExternally
    var options: WorkspaceFileEditOptions?
        get() = definedExternally
        set(value) = definedExternally
    var metadata: WorkspaceEditMetadata?
        get() = definedExternally
        set(value) = definedExternally
}

external interface WorkspaceTextEdit {
    var resource: Uri
    var edit: TextEdit
    var modelVersionId: Number?
        get() = definedExternally
        set(value) = definedExternally
    var metadata: WorkspaceEditMetadata?
        get() = definedExternally
        set(value) = definedExternally
}

external interface WorkspaceEdit {
    var edits: Array<dynamic /* WorkspaceTextEdit | WorkspaceFileEdit */>
}

external interface Rejection {
    var rejectReason: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface RenameLocation {
    var range: IRange
    var text: String
}

external interface RenameProvider {
    fun provideRenameEdits(model: ITextModel, position: Position, newName: String, token: CancellationToken): dynamic /* WorkspaceEdit | Thenable<WorkspaceEdit /* WorkspaceEdit & Rejection */>? */
    val resolveRenameLocation: ((model: ITextModel, position: Position, token: CancellationToken) -> dynamic)?
        get() = definedExternally
}

external interface Command {
    var id: String
    var title: String
    var tooltip: String?
        get() = definedExternally
        set(value) = definedExternally
    var arguments: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CodeLens {
    var range: IRange
    var id: String?
        get() = definedExternally
        set(value) = definedExternally
    var command: Command?
        get() = definedExternally
        set(value) = definedExternally
}

external interface CodeLensList {
    var lenses: Array<CodeLens>
    fun dispose()
}

external interface CodeLensProvider {
    var onDidChange: IEvent<CodeLensProvider /* this */>?
        get() = definedExternally
        set(value) = definedExternally
    fun provideCodeLenses(model: ITextModel, token: CancellationToken): dynamic /* CodeLensList? | Thenable<CodeLensList?>? */
    val resolveCodeLens: ((model: ITextModel, codeLens: CodeLens, token: CancellationToken) -> dynamic)?
        get() = definedExternally
}

external interface SemanticTokensLegend {
    var tokenTypes: Array<String>
    var tokenModifiers: Array<String>
}

external interface SemanticTokens {
    var resultId: String?
        get() = definedExternally
        set(value) = definedExternally
    var data: Uint32Array
}

external interface SemanticTokensEdit {
    var start: Number
    var deleteCount: Number
    var data: Uint32Array?
        get() = definedExternally
        set(value) = definedExternally
}

external interface SemanticTokensEdits {
    var resultId: String?
        get() = definedExternally
        set(value) = definedExternally
    var edits: Array<SemanticTokensEdit>
}

external interface DocumentSemanticTokensProvider {
    var onDidChange: IEvent<Unit>?
        get() = definedExternally
        set(value) = definedExternally
    fun getLegend(): SemanticTokensLegend
    fun provideDocumentSemanticTokens(model: ITextModel, lastResultId: String?, token: CancellationToken): dynamic /* SemanticTokens? | SemanticTokensEdits? | Thenable<dynamic /* SemanticTokens? | SemanticTokensEdits? */>? */
    fun releaseDocumentSemanticTokens(resultId: String?)
}

external interface DocumentRangeSemanticTokensProvider {
    fun getLegend(): SemanticTokensLegend
    fun provideDocumentRangeSemanticTokens(model: ITextModel, range: Range, token: CancellationToken): dynamic /* SemanticTokens? | Thenable<SemanticTokens?>? */
}

external interface ILanguageExtensionPoint {
    var id: String
    var extensions: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var filenames: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var filenamePatterns: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var firstLine: String?
        get() = definedExternally
        set(value) = definedExternally
    var aliases: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var mimetypes: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var configuration: Uri?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IMonarchLanguage {
    var tokenizer: Any
    var ignoreCase: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var unicode: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var defaultToken: String?
        get() = definedExternally
        set(value) = definedExternally
    var brackets: Array<IMonarchLanguageBracket>?
        get() = definedExternally
        set(value) = definedExternally
    var start: String?
        get() = definedExternally
        set(value) = definedExternally
    var tokenPostfix: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IExpandedMonarchLanguageRule {
    var regex: dynamic /* String? | RegExp? */
        get() = definedExternally
        set(value) = definedExternally
    var action: dynamic /* IShortMonarchLanguageAction? | IExpandedMonarchLanguageAction? | Array<IShortMonarchLanguageAction>? | Array<IExpandedMonarchLanguageAction>? */
        get() = definedExternally
        set(value) = definedExternally
    var include: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IExpandedMonarchLanguageAction {
    var group: Array<dynamic /* IShortMonarchLanguageAction | IExpandedMonarchLanguageAction | Array<IShortMonarchLanguageAction> | Array<IExpandedMonarchLanguageAction> */>?
        get() = definedExternally
        set(value) = definedExternally
    var cases: Any?
        get() = definedExternally
        set(value) = definedExternally
    var token: String?
        get() = definedExternally
        set(value) = definedExternally
    var next: String?
        get() = definedExternally
        set(value) = definedExternally
    var switchTo: String?
        get() = definedExternally
        set(value) = definedExternally
    var goBack: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bracket: String?
        get() = definedExternally
        set(value) = definedExternally
    var nextEmbedded: String?
        get() = definedExternally
        set(value) = definedExternally
    var log: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IMonarchLanguageBracket {
    var open: String
    var close: String
    var token: String
}