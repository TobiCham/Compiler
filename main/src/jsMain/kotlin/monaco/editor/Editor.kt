@file:JsQualifier("editor")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
package monaco.editor

import monaco.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import kotlin.js.Json
import kotlin.js.Promise

external interface MonacoEditor {

    fun create(domElement: HTMLElement, options: IStandaloneEditorConstructionOptions = definedExternally, override: IEditorOverrideServices = definedExternally): IStandaloneCodeEditor

    fun onDidCreateEditor(listener: (codeEditor: ICodeEditor) -> Unit): IDisposable

    fun createDiffEditor(domElement: HTMLElement, options: IDiffEditorConstructionOptions = definedExternally, override: IEditorOverrideServices = definedExternally): IStandaloneDiffEditor

    fun createDiffNavigator(diffEditor: IStandaloneDiffEditor, opts: IDiffNavigatorOptions = definedExternally): IDiffNavigator

    fun createModel(value: String, language: String = definedExternally, uri: Uri = definedExternally): ITextModel

    fun setModelLanguage(model: ITextModel, languageId: String)

    fun setModelMarkers(model: ITextModel, owner: String, markers: Array<IMarkerData>)

    fun getModelMarkers(filter: `T$3`): Array<IMarker>

    fun getModel(uri: Uri): ITextModel?

    fun getModels(): Array<ITextModel>

    fun onDidCreateModel(listener: (model: ITextModel) -> Unit): IDisposable

    fun onWillDisposeModel(listener: (model: ITextModel) -> Unit): IDisposable

    fun onDidChangeModelLanguage(listener: (e: `T$4`) -> Unit): IDisposable

    fun <T> createWebWorker(opts: IWebWorkerOptions): MonacoWebWorker<T>

    fun colorizeElement(domNode: HTMLElement, options: IColorizerElementOptions): Promise<Unit>

    fun colorize(text: String, languageId: String, options: IColorizerOptions): Promise<String>

    fun colorizeModelLine(model: ITextModel, lineNumber: Number, tabSize: Number = definedExternally): String

    fun tokenize(text: String, languageId: String): Array<Array<Token>>

    fun defineTheme(themeName: String, themeData: IStandaloneThemeData)

    fun setTheme(themeName: String)

    fun remeasureFonts()
}

external interface IDiffNavigator {
    fun canNavigate(): Boolean
    fun next()
    fun previous()
    fun dispose()
}

external interface IDiffNavigatorOptions {
    var followsCaret: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreCharChanges: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var alwaysRevealFirst: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$3` {
    var owner: String?
        get() = definedExternally
        set(value) = definedExternally
    var resource: Uri?
        get() = definedExternally
        set(value) = definedExternally
    var take: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$4` {
    var model: ITextModel
    var oldLanguage: String
}

external interface IStandaloneThemeData {
    var base: String /* 'vs' | 'vs-dark' | 'hc-black' */
    var inherit: Boolean
    var rules: Array<ITokenThemeRule>
    var encodedTokensColors: Array<String>?
        get() = definedExternally
        set(value) = definedExternally
    var colors: IColors
}

external interface IColors {
    @nativeGetter
    operator fun get(colorId: String): String?
    @nativeSetter
    operator fun set(colorId: String, value: String)
}

external interface ITokenThemeRule {
    var token: String
    var foreground: String?
        get() = definedExternally
        set(value) = definedExternally
    var background: String?
        get() = definedExternally
        set(value) = definedExternally
    var fontStyle: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface MonacoWebWorker<T> {
    fun dispose()
    fun getProxy(): Promise<T>
    fun withSyncedResources(resources: Array<Uri>): Promise<T>
}

external interface IWebWorkerOptions {
    var moduleId: String
    var createData: Any?
        get() = definedExternally
        set(value) = definedExternally
    var label: String?
        get() = definedExternally
        set(value) = definedExternally
    var host: Any?
        get() = definedExternally
        set(value) = definedExternally
    var keepIdleModels: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IActionDescriptor {
    var id: String
    var label: String
    var precondition: String?
        get() = definedExternally
        set(value) = definedExternally
    var keybindings: Array<Int>?
        get() = definedExternally
        set(value) = definedExternally
    var keybindingContext: String?
        get() = definedExternally
        set(value) = definedExternally
    var contextMenuGroupId: String?
        get() = definedExternally
        set(value) = definedExternally
    var contextMenuOrder: Number?
        get() = definedExternally
        set(value) = definedExternally
    fun run(editor: ICodeEditor, vararg args: Any): dynamic /* Unit | Promise<Unit> */
}

external interface ISemanticHighlighting {
    var enabled: dynamic /* Boolean? | String */
        get() = definedExternally
        set(value) = definedExternally
}

external interface IGlobalEditorOptions {
    var tabSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var insertSpaces: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var detectIndentation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var trimAutoWhitespace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var largeFileOptimizations: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var wordBasedSuggestions: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var semanticHighlighting: ISemanticHighlighting?
        get() = definedExternally
        set(value) = definedExternally
    var stablePeek: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxTokenizationLineLength: Number?
        get() = definedExternally
        set(value) = definedExternally
    var theme: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IStandaloneEditorConstructionOptions : IEditorConstructionOptions, IGlobalEditorOptions {
    var model: ITextModel?
        get() = definedExternally
        set(value) = definedExternally
    var value: String?
        get() = definedExternally
        set(value) = definedExternally
    var language: String?
        get() = definedExternally
        set(value) = definedExternally
    override var theme: String?
        get() = definedExternally
        set(value) = definedExternally
    var accessibilityHelpUrl: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IDiffEditorConstructionOptions : IDiffEditorOptions {
    var overflowWidgetsDomNode: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
    var theme: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IStandaloneCodeEditor : ICodeEditor {
    override fun updateOptions(newOptions: IEditorOptions /* IEditorOptions & IGlobalEditorOptions */)
    fun addCommand(keybinding: Number, handler: ICommandHandler, context: String = definedExternally): String?
    fun <T> createContextKey(key: String, defaultValue: T): IContextKey<T>
    fun addAction(descriptor: IActionDescriptor): IDisposable
}

external interface IStandaloneDiffEditor : IDiffEditor {
    fun addCommand(keybinding: Number, handler: ICommandHandler, context: String = definedExternally): String?
    fun <T> createContextKey(key: String, defaultValue: T): IContextKey<T>
    fun addAction(descriptor: IActionDescriptor): IDisposable
    override fun getOriginalEditor(): IStandaloneCodeEditor
    override fun getModifiedEditor(): IStandaloneCodeEditor
}

external interface ICommandHandler {
    @nativeInvoke
    operator fun invoke(vararg args: Any)
}

external interface IContextKey<T> {
    fun set(value: T)
    fun reset()
    fun get(): T?
}

external interface IEditorOverrideServices {
    @nativeGetter
    operator fun get(index: String): Any?
    @nativeSetter
    operator fun set(index: String, value: Any)
}

external interface `T$5` {
    var value: String
    var target: Uri
}

external interface IMarker {
    var owner: String
    var resource: Uri
    var severity: MarkerSeverity
    var code: dynamic /* String? | `T$5`? */
        get() = definedExternally
        set(value) = definedExternally
    var message: String
    var source: String?
        get() = definedExternally
        set(value) = definedExternally
    var startLineNumber: Number
    var startColumn: Number
    var endLineNumber: Number
    var endColumn: Number
    var relatedInformation: Array<IRelatedInformation>?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Array<MarkerTag>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IMarkerData {
    var code: dynamic /* String? | `T$5`? */
        get() = definedExternally
        set(value) = definedExternally
    var severity: Int
    var message: String
    var source: String?
        get() = definedExternally
        set(value) = definedExternally
    var startLineNumber: Number
    var startColumn: Number
    var endLineNumber: Number
    var endColumn: Number
    var relatedInformation: Array<IRelatedInformation>?
        get() = definedExternally
        set(value) = definedExternally
    var tags: Array<MarkerTag>?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IRelatedInformation {
    var resource: Uri
    var message: String
    var startLineNumber: Number
    var startColumn: Number
    var endLineNumber: Number
    var endColumn: Number
}

external interface IColorizerOptions {
    var tabSize: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IColorizerElementOptions : IColorizerOptions {
    var theme: String?
        get() = definedExternally
        set(value) = definedExternally
    var mimeType: String?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class ScrollbarVisibility {
    Auto /* = 1 */,
    Hidden /* = 2 */,
    Visible /* = 3 */
}

external interface ThemeColor {
    var id: String
}

external enum class OverviewRulerLane {
    Left /* = 1 */,
    Center /* = 2 */,
    Right /* = 4 */,
    Full /* = 7 */
}

external enum class MinimapPosition {
    Inline /* = 1 */,
    Gutter /* = 2 */
}

external interface IDecorationOptions {
    var color: dynamic /* String? | ThemeColor? */
        get() = definedExternally
        set(value) = definedExternally
    var darkColor: dynamic /* String? | ThemeColor? */
        get() = definedExternally
        set(value) = definedExternally
}

external interface IModelDecorationOverviewRulerOptions : IDecorationOptions {
    var position: OverviewRulerLane
}

external interface IModelDecorationMinimapOptions : IDecorationOptions {
    var position: MinimapPosition
}

external interface IModelDecorationOptions {
    var stickiness: TrackedRangeStickiness?
        get() = definedExternally
        set(value) = definedExternally
    var className: String?
        get() = definedExternally
        set(value) = definedExternally
    var glyphMarginHoverMessage: dynamic /* IMarkdownString? | Array<IMarkdownString>? */
        get() = definedExternally
        set(value) = definedExternally
    var hoverMessage: dynamic /* IMarkdownString? | Array<IMarkdownString>? */
        get() = definedExternally
        set(value) = definedExternally
    var isWholeLine: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var zIndex: Number?
        get() = definedExternally
        set(value) = definedExternally
    var overviewRuler: IModelDecorationOverviewRulerOptions?
        get() = definedExternally
        set(value) = definedExternally
    var minimap: IModelDecorationMinimapOptions?
        get() = definedExternally
        set(value) = definedExternally
    var glyphMarginClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var linesDecorationsClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var firstLineDecorationClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var marginClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var inlineClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var inlineClassNameAffectsLetterSpacing: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var beforeContentClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var afterContentClassName: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IModelDeltaDecoration {
    var range: IRange
    var options: IModelDecorationOptions
}

external interface IModelDecoration {
    var id: String
    var ownerId: Number
    var range: Range
    var options: IModelDecorationOptions
}

external interface IWordAtPosition {
    var word: String
    var startColumn: Number
    var endColumn: Number
}

external enum class EndOfLinePreference {
    TextDefined /* = 0 */,
    LF /* = 1 */,
    CRLF /* = 2 */
}

external enum class DefaultEndOfLine {
    LF /* = 1 */,
    CRLF /* = 2 */
}

external enum class EndOfLineSequence {
    LF /* = 0 */,
    CRLF /* = 1 */
}

external interface ISingleEditOperation {
    var range: IRange
    var text: String?
    var forceMoveMarkers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IIdentifiedSingleEditOperation {
    var range: IRange
    var text: String?
    var forceMoveMarkers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IValidEditOperation {
    var range: Range
    var text: String
}

external interface ICursorStateComputer {
    @nativeInvoke
    operator fun invoke(inverseEditOperations: Array<IValidEditOperation>): Array<Selection>?
}

external open class TextModelResolvedOptions {
    open var _textModelResolvedOptionsBrand: Unit
    open var tabSize: Number
    open var indentSize: Number
    open var insertSpaces: Boolean
    open var defaultEOL: DefaultEndOfLine
    open var trimAutoWhitespace: Boolean
}

external interface ITextModelUpdateOptions {
    var tabSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var indentSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var insertSpaces: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var trimAutoWhitespace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class FindMatch {
    open var _findMatchBrand: Unit
    open var range: Range
    open var matches: Array<String>?
}

external enum class TrackedRangeStickiness {
    AlwaysGrowsWhenTypingAtEdges /* = 0 */,
    NeverGrowsWhenTypingAtEdges /* = 1 */,
    GrowsOnlyWhenTypingBefore /* = 2 */,
    GrowsOnlyWhenTypingAfter /* = 3 */
}

external interface ITextModel {
    var uri: Uri
    var id: String
    fun getOptions(): TextModelResolvedOptions
    fun getVersionId(): Number
    fun getAlternativeVersionId(): Number
    fun setValue(newValue: String)
    fun getValue(eol: EndOfLinePreference = definedExternally, preserveBOM: Boolean = definedExternally): String
    fun getValueLength(eol: EndOfLinePreference = definedExternally, preserveBOM: Boolean = definedExternally): Number
    fun getValueInRange(range: IRange, eol: EndOfLinePreference = definedExternally): String
    fun getValueLengthInRange(range: IRange): Number
    fun getCharacterCountInRange(range: IRange): Number
    fun getLineCount(): Number
    fun getLineContent(lineNumber: Number): String
    fun getLineLength(lineNumber: Number): Number
    fun getLinesContent(): Array<String>
    fun getEOL(): String
    fun getLineMinColumn(lineNumber: Number): Number
    fun getLineMaxColumn(lineNumber: Number): Number
    fun getLineFirstNonWhitespaceColumn(lineNumber: Number): Number
    fun getLineLastNonWhitespaceColumn(lineNumber: Number): Number
    fun validatePosition(position: IPosition): Position
    fun modifyPosition(position: IPosition, offset: Number): Position
    fun validateRange(range: IRange): Range
    fun getOffsetAt(position: IPosition): Number
    fun getPositionAt(offset: Number): Position
    fun getFullModelRange(): Range
    fun isDisposed(): Boolean
    fun findMatches(searchString: String, searchOnlyEditableRange: Boolean, isRegex: Boolean, matchCase: Boolean, wordSeparators: String?, captureMatches: Boolean, limitResultCount: Number = definedExternally): Array<FindMatch>
    fun findMatches(searchString: String, searchScope: IRange, isRegex: Boolean, matchCase: Boolean, wordSeparators: String?, captureMatches: Boolean, limitResultCount: Number = definedExternally): Array<FindMatch>
    fun findMatches(searchString: String, searchScope: Array<IRange>, isRegex: Boolean, matchCase: Boolean, wordSeparators: String?, captureMatches: Boolean, limitResultCount: Number = definedExternally): Array<FindMatch>
    fun findNextMatch(searchString: String, searchStart: IPosition, isRegex: Boolean, matchCase: Boolean, wordSeparators: String?, captureMatches: Boolean): FindMatch?
    fun findPreviousMatch(searchString: String, searchStart: IPosition, isRegex: Boolean, matchCase: Boolean, wordSeparators: String?, captureMatches: Boolean): FindMatch?
    fun getModeId(): String
    fun getWordAtPosition(position: IPosition): IWordAtPosition?
    fun getWordUntilPosition(position: IPosition): IWordAtPosition
    fun deltaDecorations(oldDecorations: Array<String>, newDecorations: Array<IModelDeltaDecoration>, ownerId: Number = definedExternally): Array<String>
    fun getDecorationOptions(id: String): IModelDecorationOptions?
    fun getDecorationRange(id: String): Range?
    fun getLineDecorations(lineNumber: Number, ownerId: Number = definedExternally, filterOutValidation: Boolean = definedExternally): Array<IModelDecoration>
    fun getLinesDecorations(startLineNumber: Number, endLineNumber: Number, ownerId: Number = definedExternally, filterOutValidation: Boolean = definedExternally): Array<IModelDecoration>
    fun getDecorationsInRange(range: IRange, ownerId: Number = definedExternally, filterOutValidation: Boolean = definedExternally): Array<IModelDecoration>
    fun getAllDecorations(ownerId: Number = definedExternally, filterOutValidation: Boolean = definedExternally): Array<IModelDecoration>
    fun getOverviewRulerDecorations(ownerId: Number = definedExternally, filterOutValidation: Boolean = definedExternally): Array<IModelDecoration>
    fun normalizeIndentation(str: String): String
    fun updateOptions(newOpts: ITextModelUpdateOptions)
    fun detectIndentation(defaultInsertSpaces: Boolean, defaultTabSize: Number)
    fun pushStackElement()
    fun pushEditOperations(beforeCursorState: Array<Selection>?, editOperations: Array<IIdentifiedSingleEditOperation>, cursorStateComputer: ICursorStateComputer): Array<Selection>?
    fun pushEOL(eol: EndOfLineSequence)
    fun applyEdits(operations: Array<IIdentifiedSingleEditOperation>)
    fun applyEdits(operations: Array<IIdentifiedSingleEditOperation>, computeUndoEdits: Boolean): dynamic /* Unit | Array */
    fun setEOL(eol: EndOfLineSequence)
    fun onDidChangeContent(listener: (e: IModelContentChangedEvent) -> Unit): IDisposable
    fun onDidChangeDecorations(listener: (e: IModelDecorationsChangedEvent) -> Unit): IDisposable
    fun onDidChangeOptions(listener: (e: IModelOptionsChangedEvent) -> Unit): IDisposable
    fun onDidChangeLanguage(listener: (e: IModelLanguageChangedEvent) -> Unit): IDisposable
    fun onDidChangeLanguageConfiguration(listener: (e: IModelLanguageConfigurationChangedEvent) -> Unit): IDisposable
    fun onWillDispose(listener: () -> Unit): IDisposable
    fun dispose()
}

external interface IEditOperationBuilder {
    fun addEditOperation(range: IRange, text: String?, forceMoveMarkers: Boolean = definedExternally)
    fun addTrackedEditOperation(range: IRange, text: String?, forceMoveMarkers: Boolean = definedExternally)
    fun trackSelection(selection: Selection, trackPreviousOnEmpty: Boolean = definedExternally): String
}

external interface ICursorStateComputerData {
    fun getInverseEditOperations(): Array<IValidEditOperation>
    fun getTrackedSelection(id: String): Selection
}

external interface ICommand {
    fun getEditOperations(model: ITextModel, builder: IEditOperationBuilder)
    fun computeCursorState(model: ITextModel, helper: ICursorStateComputerData): Selection
}

external interface IDiffEditorModel {
    var original: ITextModel
    var modified: ITextModel
}

external interface IModelChangedEvent {
    var oldModelUrl: Uri?
    var newModelUrl: Uri?
}

external interface IDimension {
    var width: Number
    var height: Number
}

external interface IChange {
    var originalStartLineNumber: Number
    var originalEndLineNumber: Number
    var modifiedStartLineNumber: Number
    var modifiedEndLineNumber: Number
}

external interface ICharChange : IChange {
    var originalStartColumn: Number
    var originalEndColumn: Number
    var modifiedStartColumn: Number
    var modifiedEndColumn: Number
}

external interface ILineChange : IChange {
    var charChanges: Array<ICharChange>?
}

external interface IContentSizeChangedEvent {
    var contentWidth: Number
    var contentHeight: Number
    var contentWidthChanged: Boolean
    var contentHeightChanged: Boolean
}

external interface INewScrollPosition {
    var scrollLeft: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scrollTop: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IEditorAction {
    var id: String
    var label: String
    var alias: String
    fun isSupported(): Boolean
    fun run(): Promise<Unit>
}

external interface ICursorState {
    var inSelectionMode: Boolean
    var selectionStart: IPosition
    var position: IPosition
}

external interface IViewState {
    var scrollTop: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scrollTopWithoutViewZones: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scrollLeft: Number
    var firstPosition: IPosition
    var firstPositionDeltaTop: Number
}

external interface ICodeEditorViewState {
    var cursorState: Array<ICursorState>
    var viewState: IViewState
    var contributionsState: Json
}

external interface IDiffEditorViewState {
    var original: ICodeEditorViewState?
    var modified: ICodeEditorViewState?
}

external enum class ScrollType {
    Smooth /* = 0 */,
    Immediate /* = 1 */
}

external interface IEditor {
    fun onDidDispose(listener: () -> Unit): IDisposable
    fun dispose()
    fun getId(): String
    fun getEditorType(): String
    fun updateOptions(newOptions: IEditorOptions)
    fun layout(dimension: IDimension = definedExternally)
    fun focus()
    fun hasTextFocus(): Boolean
    fun getSupportedActions(): Array<IEditorAction>
    fun saveViewState(): dynamic /* ICodeEditorViewState? | IDiffEditorViewState? */
    fun restoreViewState(state: ICodeEditorViewState)
    fun restoreViewState(state: IDiffEditorViewState)
    fun getVisibleColumnFromPosition(position: IPosition): Number
    fun getPosition(): Position?
    fun setPosition(position: IPosition)
    fun revealLine(lineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLineInCenter(lineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLineInCenterIfOutsideViewport(lineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLineNearTop(lineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealPosition(position: IPosition, scrollType: ScrollType = definedExternally)
    fun revealPositionInCenter(position: IPosition, scrollType: ScrollType = definedExternally)
    fun revealPositionInCenterIfOutsideViewport(position: IPosition, scrollType: ScrollType = definedExternally)
    fun revealPositionNearTop(position: IPosition, scrollType: ScrollType = definedExternally)
    fun getSelection(): Selection?
    fun getSelections(): Array<Selection>?
    fun setSelection(selection: IRange)
    fun setSelection(selection: Range)
    fun setSelection(selection: ISelection)
    fun setSelection(selection: Selection)
    fun setSelections(selections: Any)
    fun revealLines(startLineNumber: Number, endLineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLinesInCenter(lineNumber: Number, endLineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLinesInCenterIfOutsideViewport(lineNumber: Number, endLineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealLinesNearTop(lineNumber: Number, endLineNumber: Number, scrollType: ScrollType = definedExternally)
    fun revealRange(range: IRange, scrollType: ScrollType = definedExternally)
    fun revealRangeInCenter(range: IRange, scrollType: ScrollType = definedExternally)
    fun revealRangeAtTop(range: IRange, scrollType: ScrollType = definedExternally)
    fun revealRangeInCenterIfOutsideViewport(range: IRange, scrollType: ScrollType = definedExternally)
    fun revealRangeNearTop(range: IRange, scrollType: ScrollType = definedExternally)
    fun revealRangeNearTopIfOutsideViewport(range: IRange, scrollType: ScrollType = definedExternally)
    fun trigger(source: String?, handlerId: String, payload: Any)
    fun getModel(): dynamic /* ITextModel? | IDiffEditorModel? */
    fun setModel(model: ITextModel?)
    fun setModel(model: IDiffEditorModel?)
}

external interface IEditorContribution {
    fun dispose()
    val saveViewState: (() -> Any)?
        get() = definedExternally
    val restoreViewState: ((state: Any) -> Unit)?
        get() = definedExternally
}

external object EditorType {
    var ICodeEditor: String
    var IDiffEditor: String
}

external interface IModelLanguageChangedEvent {
    var oldLanguage: String
    var newLanguage: String
}

external interface IModelLanguageConfigurationChangedEvent

external interface IModelContentChange {
    var range: IRange
    var rangeOffset: Number
    var rangeLength: Number
    var text: String
}

external interface IModelContentChangedEvent {
    var changes: Array<IModelContentChange>
    var eol: String
    var versionId: Number
    var isUndoing: Boolean
    var isRedoing: Boolean
    var isFlush: Boolean
}

external interface IModelDecorationsChangedEvent {
    var affectsMinimap: Boolean
    var affectsOverviewRuler: Boolean
}

external interface IModelOptionsChangedEvent {
    var tabSize: Boolean
    var indentSize: Boolean
    var insertSpaces: Boolean
    var trimAutoWhitespace: Boolean
}

external enum class CursorChangeReason {
    NotSet /* = 0 */,
    ContentFlush /* = 1 */,
    RecoverFromMarkers /* = 2 */,
    Explicit /* = 3 */,
    Paste /* = 4 */,
    Undo /* = 5 */,
    Redo /* = 6 */
}

external interface ICursorPositionChangedEvent {
    var position: Position
    var secondaryPositions: Array<Position>
    var reason: CursorChangeReason
    var source: String
}

external interface ICursorSelectionChangedEvent {
    var selection: Selection
    var secondarySelections: Array<Selection>
    var modelVersionId: Number
    var oldSelections: Array<Selection>?
    var oldModelVersionId: Number
    var source: String
    var reason: CursorChangeReason
}

external enum class AccessibilitySupport {
    Unknown /* = 0 */,
    Disabled /* = 1 */,
    Enabled /* = 2 */
}

external enum class EditorAutoIndentStrategy {
    None /* = 0 */,
    Keep /* = 1 */,
    Brackets /* = 2 */,
    Advanced /* = 3 */,
    Full /* = 4 */
}

external interface IEditorOptions {
    var inDiffEditor: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ariaLabel: String?
        get() = definedExternally
        set(value) = definedExternally
    var tabIndex: Number?
        get() = definedExternally
        set(value) = definedExternally
    var rulers: Array<dynamic /* Number | IRulerOption */>?
        get() = definedExternally
        set(value) = definedExternally
    var wordSeparators: String?
        get() = definedExternally
        set(value) = definedExternally
    var selectionClipboard: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var lineNumbers: dynamic /* String | String | String | String | ((lineNumber: Number) -> String)? */
        get() = definedExternally
        set(value) = definedExternally
    var cursorSurroundingLines: Number?
        get() = definedExternally
        set(value) = definedExternally
    var cursorSurroundingLinesStyle: String? /* 'default' | 'all' */
        get() = definedExternally
        set(value) = definedExternally
    var renderFinalNewline: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var unusualLineTerminators: String? /* 'off' | 'prompt' | 'auto' */
        get() = definedExternally
        set(value) = definedExternally
    var selectOnLineNumbers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var lineNumbersMinChars: Number?
        get() = definedExternally
        set(value) = definedExternally
    var glyphMargin: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var lineDecorationsWidth: dynamic /* Number? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var revealHorizontalRightPadding: Number?
        get() = definedExternally
        set(value) = definedExternally
    var roundedSelection: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var extraEditorClassName: String?
        get() = definedExternally
        set(value) = definedExternally
    var readOnly: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renameOnType: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderValidationDecorations: String? /* 'editable' | 'on' | 'off' */
        get() = definedExternally
        set(value) = definedExternally
    var scrollbar: IEditorScrollbarOptions?
        get() = definedExternally
        set(value) = definedExternally
    var minimap: IEditorMinimapOptions?
        get() = definedExternally
        set(value) = definedExternally
    var find: IEditorFindOptions?
        get() = definedExternally
        set(value) = definedExternally
    var fixedOverflowWidgets: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var overviewRulerLanes: Number?
        get() = definedExternally
        set(value) = definedExternally
    var overviewRulerBorder: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cursorBlinking: String? /* 'blink' | 'smooth' | 'phase' | 'expand' | 'solid' */
        get() = definedExternally
        set(value) = definedExternally
    var mouseWheelZoom: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var mouseStyle: String? /* 'text' | 'default' | 'copy' */
        get() = definedExternally
        set(value) = definedExternally
    var cursorSmoothCaretAnimation: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cursorStyle: String? /* 'line' | 'block' | 'underline' | 'line-thin' | 'block-outline' | 'underline-thin' */
        get() = definedExternally
        set(value) = definedExternally
    var cursorWidth: Number?
        get() = definedExternally
        set(value) = definedExternally
    var fontLigatures: dynamic /* Boolean? | String? */
        get() = definedExternally
        set(value) = definedExternally
    var disableLayerHinting: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var disableMonospaceOptimizations: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var hideCursorInOverviewRuler: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var scrollBeyondLastLine: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var scrollBeyondLastColumn: Number?
        get() = definedExternally
        set(value) = definedExternally
    var smoothScrolling: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var automaticLayout: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var wordWrap: String? /* 'off' | 'on' | 'wordWrapColumn' | 'bounded' */
        get() = definedExternally
        set(value) = definedExternally
    var wordWrapColumn: Number?
        get() = definedExternally
        set(value) = definedExternally
    var wordWrapMinified: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var wrappingIndent: String? /* 'none' | 'same' | 'indent' | 'deepIndent' */
        get() = definedExternally
        set(value) = definedExternally
    var wrappingStrategy: String? /* 'simple' | 'advanced' */
        get() = definedExternally
        set(value) = definedExternally
    var wordWrapBreakBeforeCharacters: String?
        get() = definedExternally
        set(value) = definedExternally
    var wordWrapBreakAfterCharacters: String?
        get() = definedExternally
        set(value) = definedExternally
    var stopRenderingLineAfter: Number?
        get() = definedExternally
        set(value) = definedExternally
    var hover: IEditorHoverOptions?
        get() = definedExternally
        set(value) = definedExternally
    var links: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var colorDecorators: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var comments: IEditorCommentsOptions?
        get() = definedExternally
        set(value) = definedExternally
    var contextmenu: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var mouseWheelScrollSensitivity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var fastScrollSensitivity: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scrollPredominantAxis: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var columnSelection: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var multiCursorModifier: String? /* 'ctrlCmd' | 'alt' */
        get() = definedExternally
        set(value) = definedExternally
    var multiCursorMergeOverlapping: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var multiCursorPaste: String? /* 'spread' | 'full' */
        get() = definedExternally
        set(value) = definedExternally
    var accessibilitySupport: String? /* 'auto' | 'off' | 'on' */
        get() = definedExternally
        set(value) = definedExternally
    var accessibilityPageSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var suggest: ISuggestOptions?
        get() = definedExternally
        set(value) = definedExternally
    var gotoLocation: IGotoLocationOptions?
        get() = definedExternally
        set(value) = definedExternally
    var quickSuggestions: dynamic /* Boolean? | IQuickSuggestionsOptions? */
        get() = definedExternally
        set(value) = definedExternally
    var quickSuggestionsDelay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var padding: IEditorPaddingOptions?
        get() = definedExternally
        set(value) = definedExternally
    var parameterHints: IEditorParameterHintOptions?
        get() = definedExternally
        set(value) = definedExternally
    var autoClosingBrackets: String? /* 'always' | 'languageDefined' | 'beforeWhitespace' | 'never' */
        get() = definedExternally
        set(value) = definedExternally
    var autoClosingQuotes: String? /* 'always' | 'languageDefined' | 'beforeWhitespace' | 'never' */
        get() = definedExternally
        set(value) = definedExternally
    var autoClosingOvertype: String? /* 'always' | 'auto' | 'never' */
        get() = definedExternally
        set(value) = definedExternally
    var autoSurround: String? /* 'languageDefined' | 'quotes' | 'brackets' | 'never' */
        get() = definedExternally
        set(value) = definedExternally
    var autoIndent: String? /* 'none' | 'keep' | 'brackets' | 'advanced' | 'full' */
        get() = definedExternally
        set(value) = definedExternally
    var formatOnType: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var formatOnPaste: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var dragAndDrop: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var suggestOnTriggerCharacters: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var acceptSuggestionOnEnter: String? /* 'on' | 'smart' | 'off' */
        get() = definedExternally
        set(value) = definedExternally
    var acceptSuggestionOnCommitCharacter: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var snippetSuggestions: String? /* 'top' | 'bottom' | 'inline' | 'none' */
        get() = definedExternally
        set(value) = definedExternally
    var emptySelectionClipboard: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var copyWithSyntaxHighlighting: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var suggestSelection: String? /* 'first' | 'recentlyUsed' | 'recentlyUsedByPrefix' */
        get() = definedExternally
        set(value) = definedExternally
    var suggestFontSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var suggestLineHeight: Number?
        get() = definedExternally
        set(value) = definedExternally
    var tabCompletion: String? /* 'on' | 'off' | 'onlySnippets' */
        get() = definedExternally
        set(value) = definedExternally
    var selectionHighlight: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var occurrencesHighlight: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var codeLens: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var lightbulb: IEditorLightbulbOptions?
        get() = definedExternally
        set(value) = definedExternally
    var codeActionsOnSaveTimeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var folding: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var foldingStrategy: String? /* 'auto' | 'indentation' */
        get() = definedExternally
        set(value) = definedExternally
    var foldingHighlight: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showFoldingControls: String? /* 'always' | 'mouseover' */
        get() = definedExternally
        set(value) = definedExternally
    var unfoldOnClickAfterEndOfLine: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var matchBrackets: String? /* 'never' | 'near' | 'always' */
        get() = definedExternally
        set(value) = definedExternally
    var renderWhitespace: String? /* 'none' | 'boundary' | 'selection' | 'trailing' | 'all' */
        get() = definedExternally
        set(value) = definedExternally
    var renderControlCharacters: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderIndentGuides: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var highlightActiveIndentGuide: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderLineHighlight: String? /* 'none' | 'gutter' | 'line' | 'all' */
        get() = definedExternally
        set(value) = definedExternally
    var renderLineHighlightOnlyWhenFocus: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var useTabStops: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var fontFamily: String?
        get() = definedExternally
        set(value) = definedExternally
    var fontWeight: String?
        get() = definedExternally
        set(value) = definedExternally
    var fontSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var lineHeight: Number?
        get() = definedExternally
        set(value) = definedExternally
    var letterSpacing: Number?
        get() = definedExternally
        set(value) = definedExternally
    var showUnused: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var peekWidgetDefaultFocus: String? /* 'tree' | 'editor' */
        get() = definedExternally
        set(value) = definedExternally
    var definitionLinkOpensInPeek: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showDeprecated: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IDiffEditorOptions : IEditorOptions {
    var enableSplitViewResizing: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderSideBySide: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxComputationTime: Number?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreTrimWhitespace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var renderIndicators: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var originalEditable: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var originalCodeLens: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var modifiedCodeLens: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external open class ConfigurationChangedEvent {
    open fun hasChanged(id: EditorOption): Boolean
}

external interface IComputedEditorOptions {
    fun <T : EditorOption> get(id: T): FindComputedEditorOptionValueById<T>
}

external interface IEditorOption<K1 : EditorOption, V> {
    var id: K1
    var name: String
    var defaultValue: V
}

external interface IEditorCommentsOptions {
    var insertSpace: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var ignoreEmptyLines: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class TextEditorCursorBlinkingStyle {
    Hidden /* = 0 */,
    Blink /* = 1 */,
    Smooth /* = 2 */,
    Phase /* = 3 */,
    Expand /* = 4 */,
    Solid /* = 5 */
}

external enum class TextEditorCursorStyle {
    Line /* = 1 */,
    Block /* = 2 */,
    Underline /* = 3 */,
    LineThin /* = 4 */,
    BlockOutline /* = 5 */,
    UnderlineThin /* = 6 */
}

external interface IEditorFindOptions {
    var cursorMoveOnType: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var seedSearchStringFromSelection: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var autoFindInSelection: String? /* 'never' | 'always' | 'multiline' */
        get() = definedExternally
        set(value) = definedExternally
    var addExtraSpaceOnTop: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var loop: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IGotoLocationOptions {
    var multiple: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var multipleDefinitions: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var multipleTypeDefinitions: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var multipleDeclarations: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var multipleImplementations: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var multipleReferences: String? /* 'peek' | 'gotoAndPeek' | 'goto' */
        get() = definedExternally
        set(value) = definedExternally
    var alternativeDefinitionCommand: String?
        get() = definedExternally
        set(value) = definedExternally
    var alternativeTypeDefinitionCommand: String?
        get() = definedExternally
        set(value) = definedExternally
    var alternativeDeclarationCommand: String?
        get() = definedExternally
        set(value) = definedExternally
    var alternativeImplementationCommand: String?
        get() = definedExternally
        set(value) = definedExternally
    var alternativeReferenceCommand: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IEditorHoverOptions {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var delay: Number?
        get() = definedExternally
        set(value) = definedExternally
    var sticky: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface OverviewRulerPosition {
    var width: Number
    var height: Number
    var top: Number
    var right: Number
}

external enum class RenderMinimap {
    None /* = 0 */,
    Text /* = 1 */,
    Blocks /* = 2 */
}

external interface EditorLayoutInfo {
    var width: Number
    var height: Number
    var glyphMarginLeft: Number
    var glyphMarginWidth: Number
    var lineNumbersLeft: Number
    var lineNumbersWidth: Number
    var decorationsLeft: Number
    var decorationsWidth: Number
    var contentLeft: Number
    var contentWidth: Number
    var minimap: EditorMinimapLayoutInfo
    var viewportColumn: Number
    var isWordWrapMinified: Boolean
    var isViewportWrapping: Boolean
    var wrappingColumn: Number
    var verticalScrollbarWidth: Number
    var horizontalScrollbarHeight: Number
    var overviewRuler: OverviewRulerPosition
}

external interface EditorMinimapLayoutInfo {
    var renderMinimap: RenderMinimap
    var minimapLeft: Number
    var minimapWidth: Number
    var minimapHeightIsEditorHeight: Boolean
    var minimapIsSampling: Boolean
    var minimapScale: Number
    var minimapLineHeight: Number
    var minimapCanvasInnerWidth: Number
    var minimapCanvasInnerHeight: Number
    var minimapCanvasOuterWidth: Number
    var minimapCanvasOuterHeight: Number
}

external interface IEditorLightbulbOptions {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IEditorMinimapOptions {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var side: String? /* 'right' | 'left' */
        get() = definedExternally
        set(value) = definedExternally
    var size: String? /* 'proportional' | 'fill' | 'fit' */
        get() = definedExternally
        set(value) = definedExternally
    var showSlider: String? /* 'always' | 'mouseover' */
        get() = definedExternally
        set(value) = definedExternally
    var renderCharacters: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxColumn: Number?
        get() = definedExternally
        set(value) = definedExternally
    var scale: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IEditorPaddingOptions {
    var top: Number?
        get() = definedExternally
        set(value) = definedExternally
    var bottom: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InternalEditorPaddingOptions {
    var top: Number
    var bottom: Number
}

external interface IEditorParameterHintOptions {
    var enabled: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var cycle: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IQuickSuggestionsOptions {
    var other: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var comments: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var strings: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class RenderLineNumbersType {
    Off /* = 0 */,
    On /* = 1 */,
    Relative /* = 2 */,
    Interval /* = 3 */,
    Custom /* = 4 */
}

external interface InternalEditorRenderLineNumbersOptions {
    var renderType: RenderLineNumbersType
    var renderFn: ((lineNumber: Number) -> String)?
}

external interface IRulerOption {
    var column: Number
    var color: String?
}

external interface IEditorScrollbarOptions {
    var arrowSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var vertical: String? /* 'auto' | 'visible' | 'hidden' */
        get() = definedExternally
        set(value) = definedExternally
    var horizontal: String? /* 'auto' | 'visible' | 'hidden' */
        get() = definedExternally
        set(value) = definedExternally
    var useShadows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var verticalHasArrows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var horizontalHasArrows: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var handleMouseWheel: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var alwaysConsumeMouseWheel: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var horizontalScrollbarSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var verticalScrollbarSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var verticalSliderSize: Number?
        get() = definedExternally
        set(value) = definedExternally
    var horizontalSliderSize: Number?
        get() = definedExternally
        set(value) = definedExternally
}

external interface InternalEditorScrollbarOptions {
    var arrowSize: Number
    var vertical: ScrollbarVisibility
    var horizontal: ScrollbarVisibility
    var useShadows: Boolean
    var verticalHasArrows: Boolean
    var horizontalHasArrows: Boolean
    var handleMouseWheel: Boolean
    var alwaysConsumeMouseWheel: Boolean
    var horizontalScrollbarSize: Number
    var horizontalSliderSize: Number
    var verticalScrollbarSize: Number
    var verticalSliderSize: Number
}

external interface `T$6` {
    var visible: Boolean?
        get() = definedExternally
        set(value) = definedExternally
}

external interface ISuggestOptions {
    var insertMode: String? /* 'insert' | 'replace' */
        get() = definedExternally
        set(value) = definedExternally
    var filterGraceful: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var snippetsPreventQuickSuggestions: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var localityBonus: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var shareSuggestSelections: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showIcons: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var maxVisibleSuggestions: Number?
        get() = definedExternally
        set(value) = definedExternally
    var showMethods: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showFunctions: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showConstructors: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showFields: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showVariables: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showClasses: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showStructs: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showInterfaces: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showModules: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showProperties: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showEvents: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showOperators: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showUnits: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showValues: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showConstants: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showEnums: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showEnumMembers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showKeywords: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showWords: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showColors: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showFiles: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showReferences: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showFolders: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showTypeParameters: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showIssues: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showUsers: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var showSnippets: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var statusBar: `T$6`?
        get() = definedExternally
        set(value) = definedExternally
}

external enum class WrappingIndent {
    None /* = 0 */,
    Same /* = 1 */,
    Indent /* = 2 */,
    DeepIndent /* = 3 */
}

external interface EditorWrappingInfo {
    var isDominatedByLongLines: Boolean
    var isWordWrapMinified: Boolean
    var isViewportWrapping: Boolean
    var wrappingColumn: Number
}

external enum class EditorOption {
    acceptSuggestionOnCommitCharacter /* = 0 */,
    acceptSuggestionOnEnter /* = 1 */,
    accessibilitySupport /* = 2 */,
    accessibilityPageSize /* = 3 */,
    ariaLabel /* = 4 */,
    autoClosingBrackets /* = 5 */,
    autoClosingOvertype /* = 6 */,
    autoClosingQuotes /* = 7 */,
    autoIndent /* = 8 */,
    automaticLayout /* = 9 */,
    autoSurround /* = 10 */,
    codeLens /* = 11 */,
    colorDecorators /* = 12 */,
    columnSelection /* = 13 */,
    comments /* = 14 */,
    contextmenu /* = 15 */,
    copyWithSyntaxHighlighting /* = 16 */,
    cursorBlinking /* = 17 */,
    cursorSmoothCaretAnimation /* = 18 */,
    cursorStyle /* = 19 */,
    cursorSurroundingLines /* = 20 */,
    cursorSurroundingLinesStyle /* = 21 */,
    cursorWidth /* = 22 */,
    disableLayerHinting /* = 23 */,
    disableMonospaceOptimizations /* = 24 */,
    dragAndDrop /* = 25 */,
    emptySelectionClipboard /* = 26 */,
    extraEditorClassName /* = 27 */,
    fastScrollSensitivity /* = 28 */,
    find /* = 29 */,
    fixedOverflowWidgets /* = 30 */,
    folding /* = 31 */,
    foldingStrategy /* = 32 */,
    foldingHighlight /* = 33 */,
    unfoldOnClickAfterEndOfLine /* = 34 */,
    fontFamily /* = 35 */,
    fontInfo /* = 36 */,
    fontLigatures /* = 37 */,
    fontSize /* = 38 */,
    fontWeight /* = 39 */,
    formatOnPaste /* = 40 */,
    formatOnType /* = 41 */,
    glyphMargin /* = 42 */,
    gotoLocation /* = 43 */,
    hideCursorInOverviewRuler /* = 44 */,
    highlightActiveIndentGuide /* = 45 */,
    hover /* = 46 */,
    inDiffEditor /* = 47 */,
    letterSpacing /* = 48 */,
    lightbulb /* = 49 */,
    lineDecorationsWidth /* = 50 */,
    lineHeight /* = 51 */,
    lineNumbers /* = 52 */,
    lineNumbersMinChars /* = 53 */,
    links /* = 54 */,
    matchBrackets /* = 55 */,
    minimap /* = 56 */,
    mouseStyle /* = 57 */,
    mouseWheelScrollSensitivity /* = 58 */,
    mouseWheelZoom /* = 59 */,
    multiCursorMergeOverlapping /* = 60 */,
    multiCursorModifier /* = 61 */,
    multiCursorPaste /* = 62 */,
    occurrencesHighlight /* = 63 */,
    overviewRulerBorder /* = 64 */,
    overviewRulerLanes /* = 65 */,
    padding /* = 66 */,
    parameterHints /* = 67 */,
    peekWidgetDefaultFocus /* = 68 */,
    definitionLinkOpensInPeek /* = 69 */,
    quickSuggestions /* = 70 */,
    quickSuggestionsDelay /* = 71 */,
    readOnly /* = 72 */,
    renameOnType /* = 73 */,
    renderControlCharacters /* = 74 */,
    renderIndentGuides /* = 75 */,
    renderFinalNewline /* = 76 */,
    renderLineHighlight /* = 77 */,
    renderLineHighlightOnlyWhenFocus /* = 78 */,
    renderValidationDecorations /* = 79 */,
    renderWhitespace /* = 80 */,
    revealHorizontalRightPadding /* = 81 */,
    roundedSelection /* = 82 */,
    rulers /* = 83 */,
    scrollbar /* = 84 */,
    scrollBeyondLastColumn /* = 85 */,
    scrollBeyondLastLine /* = 86 */,
    scrollPredominantAxis /* = 87 */,
    selectionClipboard /* = 88 */,
    selectionHighlight /* = 89 */,
    selectOnLineNumbers /* = 90 */,
    showFoldingControls /* = 91 */,
    showUnused /* = 92 */,
    snippetSuggestions /* = 93 */,
    smoothScrolling /* = 94 */,
    stopRenderingLineAfter /* = 95 */,
    suggest /* = 96 */,
    suggestFontSize /* = 97 */,
    suggestLineHeight /* = 98 */,
    suggestOnTriggerCharacters /* = 99 */,
    suggestSelection /* = 100 */,
    tabCompletion /* = 101 */,
    tabIndex /* = 102 */,
    unusualLineTerminators /* = 103 */,
    useTabStops /* = 104 */,
    wordSeparators /* = 105 */,
    wordWrap /* = 106 */,
    wordWrapBreakAfterCharacters /* = 107 */,
    wordWrapBreakBeforeCharacters /* = 108 */,
    wordWrapColumn /* = 109 */,
    wordWrapMinified /* = 110 */,
    wrappingIndent /* = 111 */,
    wrappingStrategy /* = 112 */,
    showDeprecated /* = 113 */,
    editorClassName /* = 114 */,
    pixelRatio /* = 115 */,
    tabFocusMode /* = 116 */,
    layoutInfo /* = 117 */,
    wrappingInfo /* = 118 */
}

external object EditorOptions {
    var acceptSuggestionOnCommitCharacter: IEditorOption<EditorOption, Boolean>
    var acceptSuggestionOnEnter: IEditorOption<EditorOption, String /* 'on' | 'off' | 'smart' */>
    var accessibilitySupport: IEditorOption<EditorOption, AccessibilitySupport>
    var accessibilityPageSize: IEditorOption<EditorOption, Number>
    var ariaLabel: IEditorOption<EditorOption, String>
    var autoClosingBrackets: IEditorOption<EditorOption, String /* 'always' | 'languageDefined' | 'beforeWhitespace' | 'never' */>
    var autoClosingOvertype: IEditorOption<EditorOption, String /* 'always' | 'auto' | 'never' */>
    var autoClosingQuotes: IEditorOption<EditorOption, String /* 'always' | 'languageDefined' | 'beforeWhitespace' | 'never' */>
    var autoIndent: IEditorOption<EditorOption, EditorAutoIndentStrategy>
    var automaticLayout: IEditorOption<EditorOption, Boolean>
    var autoSurround: IEditorOption<EditorOption, String /* 'languageDefined' | 'quotes' | 'brackets' | 'never' */>
    var codeLens: IEditorOption<EditorOption, Boolean>
    var colorDecorators: IEditorOption<EditorOption, Boolean>
    var columnSelection: IEditorOption<EditorOption, Boolean>
    var comments: IEditorOption<EditorOption, EditorCommentsOptions>
    var contextmenu: IEditorOption<EditorOption, Boolean>
    var copyWithSyntaxHighlighting: IEditorOption<EditorOption, Boolean>
    var cursorBlinking: IEditorOption<EditorOption, TextEditorCursorBlinkingStyle>
    var cursorSmoothCaretAnimation: IEditorOption<EditorOption, Boolean>
    var cursorStyle: IEditorOption<EditorOption, TextEditorCursorStyle>
    var cursorSurroundingLines: IEditorOption<EditorOption, Number>
    var cursorSurroundingLinesStyle: IEditorOption<EditorOption, String /* 'default' | 'all' */>
    var cursorWidth: IEditorOption<EditorOption, Number>
    var disableLayerHinting: IEditorOption<EditorOption, Boolean>
    var disableMonospaceOptimizations: IEditorOption<EditorOption, Boolean>
    var dragAndDrop: IEditorOption<EditorOption, Boolean>
    var emptySelectionClipboard: IEditorOption<EditorOption, Boolean>
    var extraEditorClassName: IEditorOption<EditorOption, String>
    var fastScrollSensitivity: IEditorOption<EditorOption, Number>
    var find: IEditorOption<EditorOption, EditorFindOptions>
    var fixedOverflowWidgets: IEditorOption<EditorOption, Boolean>
    var folding: IEditorOption<EditorOption, Boolean>
    var foldingStrategy: IEditorOption<EditorOption, String /* 'auto' | 'indentation' */>
    var foldingHighlight: IEditorOption<EditorOption, Boolean>
    var unfoldOnClickAfterEndOfLine: IEditorOption<EditorOption, Boolean>
    var fontFamily: IEditorOption<EditorOption, String>
    var fontInfo: IEditorOption<EditorOption, FontInfo>
    var fontLigatures2: IEditorOption<EditorOption, String>
    var fontSize: IEditorOption<EditorOption, Number>
    var fontWeight: IEditorOption<EditorOption, String>
    var formatOnPaste: IEditorOption<EditorOption, Boolean>
    var formatOnType: IEditorOption<EditorOption, Boolean>
    var glyphMargin: IEditorOption<EditorOption, Boolean>
    var gotoLocation: IEditorOption<EditorOption, GoToLocationOptions>
    var hideCursorInOverviewRuler: IEditorOption<EditorOption, Boolean>
    var highlightActiveIndentGuide: IEditorOption<EditorOption, Boolean>
    var hover: IEditorOption<EditorOption, EditorHoverOptions>
    var inDiffEditor: IEditorOption<EditorOption, Boolean>
    var letterSpacing: IEditorOption<EditorOption, Number>
    var lightbulb: IEditorOption<EditorOption, EditorLightbulbOptions>
    var lineDecorationsWidth: IEditorOption<EditorOption, dynamic /* String | Number */>
    var lineHeight: IEditorOption<EditorOption, Number>
    var lineNumbers: IEditorOption<EditorOption, InternalEditorRenderLineNumbersOptions>
    var lineNumbersMinChars: IEditorOption<EditorOption, Number>
    var links: IEditorOption<EditorOption, Boolean>
    var matchBrackets: IEditorOption<EditorOption, String /* 'always' | 'never' | 'near' */>
    var minimap: IEditorOption<EditorOption, EditorMinimapOptions>
    var mouseStyle: IEditorOption<EditorOption, String /* 'default' | 'text' | 'copy' */>
    var mouseWheelScrollSensitivity: IEditorOption<EditorOption, Number>
    var mouseWheelZoom: IEditorOption<EditorOption, Boolean>
    var multiCursorMergeOverlapping: IEditorOption<EditorOption, Boolean>
    var multiCursorModifier: IEditorOption<EditorOption, String /* 'altKey' | 'metaKey' | 'ctrlKey' */>
    var multiCursorPaste: IEditorOption<EditorOption, String /* 'spread' | 'full' */>
    var occurrencesHighlight: IEditorOption<EditorOption, Boolean>
    var overviewRulerBorder: IEditorOption<EditorOption, Boolean>
    var overviewRulerLanes: IEditorOption<EditorOption, Number>
    var padding: IEditorOption<EditorOption, InternalEditorPaddingOptions>
    var parameterHints: IEditorOption<EditorOption, InternalParameterHintOptions>
    var peekWidgetDefaultFocus: IEditorOption<EditorOption, String /* 'tree' | 'editor' */>
    var definitionLinkOpensInPeek: IEditorOption<EditorOption, Boolean>
    var quickSuggestions: IEditorOption<EditorOption, dynamic /* Boolean | Readonly<Required<IQuickSuggestionsOptions>> */>
    var quickSuggestionsDelay: IEditorOption<EditorOption, Number>
    var readOnly: IEditorOption<EditorOption, Boolean>
    var renameOnType: IEditorOption<EditorOption, Boolean>
    var renderControlCharacters: IEditorOption<EditorOption, Boolean>
    var renderIndentGuides: IEditorOption<EditorOption, Boolean>
    var renderFinalNewline: IEditorOption<EditorOption, Boolean>
    var renderLineHighlight: IEditorOption<EditorOption, String /* 'all' | 'line' | 'none' | 'gutter' */>
    var renderLineHighlightOnlyWhenFocus: IEditorOption<EditorOption, Boolean>
    var renderValidationDecorations: IEditorOption<EditorOption, String /* 'on' | 'off' | 'editable' */>
    var renderWhitespace: IEditorOption<EditorOption, String /* 'all' | 'none' | 'boundary' | 'selection' | 'trailing' */>
    var revealHorizontalRightPadding: IEditorOption<EditorOption, Number>
    var roundedSelection: IEditorOption<EditorOption, Boolean>
    var rulers: IEditorOption<EditorOption, Any>
    var scrollbar: IEditorOption<EditorOption, InternalEditorScrollbarOptions>
    var scrollBeyondLastColumn: IEditorOption<EditorOption, Number>
    var scrollBeyondLastLine: IEditorOption<EditorOption, Boolean>
    var scrollPredominantAxis: IEditorOption<EditorOption, Boolean>
    var selectionClipboard: IEditorOption<EditorOption, Boolean>
    var selectionHighlight: IEditorOption<EditorOption, Boolean>
    var selectOnLineNumbers: IEditorOption<EditorOption, Boolean>
    var showFoldingControls: IEditorOption<EditorOption, String /* 'always' | 'mouseover' */>
    var showUnused: IEditorOption<EditorOption, Boolean>
    var showDeprecated: IEditorOption<EditorOption, Boolean>
    var snippetSuggestions: IEditorOption<EditorOption, String /* 'none' | 'top' | 'bottom' | 'inline' */>
    var smoothScrolling: IEditorOption<EditorOption, Boolean>
    var stopRenderingLineAfter: IEditorOption<EditorOption, Number>
    var suggest: IEditorOption<EditorOption, InternalSuggestOptions>
    var suggestFontSize: IEditorOption<EditorOption, Number>
    var suggestLineHeight: IEditorOption<EditorOption, Number>
    var suggestOnTriggerCharacters: IEditorOption<EditorOption, Boolean>
    var suggestSelection: IEditorOption<EditorOption, String /* 'first' | 'recentlyUsed' | 'recentlyUsedByPrefix' */>
    var tabCompletion: IEditorOption<EditorOption, String /* 'on' | 'off' | 'onlySnippets' */>
    var tabIndex: IEditorOption<EditorOption, Number>
    var unusualLineTerminators: IEditorOption<EditorOption, String /* 'off' | 'prompt' | 'auto' */>
    var useTabStops: IEditorOption<EditorOption, Boolean>
    var wordSeparators: IEditorOption<EditorOption, String>
    var wordWrap: IEditorOption<EditorOption, String /* 'on' | 'off' | 'wordWrapColumn' | 'bounded' */>
    var wordWrapBreakAfterCharacters: IEditorOption<EditorOption, String>
    var wordWrapBreakBeforeCharacters: IEditorOption<EditorOption, String>
    var wordWrapColumn: IEditorOption<EditorOption, Number>
    var wordWrapMinified: IEditorOption<EditorOption, Boolean>
    var wrappingIndent: IEditorOption<EditorOption, WrappingIndent>
    var wrappingStrategy: IEditorOption<EditorOption, String /* 'simple' | 'advanced' */>
    var editorClassName: IEditorOption<EditorOption, String>
    var pixelRatio: IEditorOption<EditorOption, Number>
    var tabFocusMode: IEditorOption<EditorOption, Boolean>
    var layoutInfo: IEditorOption<EditorOption, EditorLayoutInfo>
    var wrappingInfo: IEditorOption<EditorOption, EditorWrappingInfo>
}

external interface IViewZone {
    var afterLineNumber: Number
    var afterColumn: Number?
        get() = definedExternally
        set(value) = definedExternally
    var suppressMouseDown: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var heightInLines: Number?
        get() = definedExternally
        set(value) = definedExternally
    var heightInPx: Number?
        get() = definedExternally
        set(value) = definedExternally
    var minWidthInPx: Number?
        get() = definedExternally
        set(value) = definedExternally
    var domNode: HTMLElement
    var marginDomNode: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
    var onDomNodeTop: ((top: Number) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var onComputedHeight: ((height: Number) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IViewZoneChangeAccessor {
    fun addZone(zone: IViewZone): String
    fun removeZone(id: String)
    fun layoutZone(id: String)
}

external enum class ContentWidgetPositionPreference {
    EXACT /* = 0 */,
    ABOVE /* = 1 */,
    BELOW /* = 2 */
}

external interface IContentWidgetPosition {
    var position: IPosition?
    var range: IRange?
        get() = definedExternally
        set(value) = definedExternally
    var preference: Array<ContentWidgetPositionPreference>
}

external interface IContentWidget {
    var allowEditorOverflow: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var suppressMouseDown: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    fun getId(): String
    fun getDomNode(): HTMLElement
    fun getPosition(): IContentWidgetPosition?
}

external enum class OverlayWidgetPositionPreference {
    TOP_RIGHT_CORNER /* = 0 */,
    BOTTOM_RIGHT_CORNER /* = 1 */,
    TOP_CENTER /* = 2 */
}

external interface IOverlayWidgetPosition {
    var preference: OverlayWidgetPositionPreference?
}

external interface IOverlayWidget {
    fun getId(): String
    fun getDomNode(): HTMLElement
    fun getPosition(): IOverlayWidgetPosition?
}

external enum class MouseTargetType {
    UNKNOWN /* = 0 */,
    TEXTAREA /* = 1 */,
    GUTTER_GLYPH_MARGIN /* = 2 */,
    GUTTER_LINE_NUMBERS /* = 3 */,
    GUTTER_LINE_DECORATIONS /* = 4 */,
    GUTTER_VIEW_ZONE /* = 5 */,
    CONTENT_TEXT /* = 6 */,
    CONTENT_EMPTY /* = 7 */,
    CONTENT_VIEW_ZONE /* = 8 */,
    CONTENT_WIDGET /* = 9 */,
    OVERVIEW_RULER /* = 10 */,
    SCROLLBAR /* = 11 */,
    OVERLAY_WIDGET /* = 12 */,
    OUTSIDE_EDITOR /* = 13 */
}

external interface IMouseTarget {
    var element: Element?
    var type: MouseTargetType
    var position: Position?
    var mouseColumn: Number
    var range: Range?
    var detail: Any
}

external interface IEditorMouseEvent {
    var event: IMouseEvent
    var target: IMouseTarget
}

external interface IPartialEditorMouseEvent {
    var event: IMouseEvent
    var target: IMouseTarget?
}

external interface IPasteEvent {
    var range: Range
    var mode: String?
}

external interface IEditorConstructionOptions : IEditorOptions {
    var dimension: IDimension?
        get() = definedExternally
        set(value) = definedExternally
    var overflowWidgetsDomNode: HTMLElement?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$7` {
    var preserveBOM: Boolean
    var lineEnding: String
}

external interface `T$8` {
    var top: Number
    var left: Number
    var height: Number
}

external interface ICodeEditor : IEditor {
    fun onDidChangeModelContent(listener: (e: IModelContentChangedEvent) -> Unit): IDisposable
    fun onDidChangeModelLanguage(listener: (e: IModelLanguageChangedEvent) -> Unit): IDisposable
    fun onDidChangeModelLanguageConfiguration(listener: (e: IModelLanguageConfigurationChangedEvent) -> Unit): IDisposable
    fun onDidChangeModelOptions(listener: (e: IModelOptionsChangedEvent) -> Unit): IDisposable
    fun onDidChangeConfiguration(listener: (e: ConfigurationChangedEvent) -> Unit): IDisposable
    fun onDidChangeCursorPosition(listener: (e: ICursorPositionChangedEvent) -> Unit): IDisposable
    fun onDidChangeCursorSelection(listener: (e: ICursorSelectionChangedEvent) -> Unit): IDisposable
    fun onDidChangeModel(listener: (e: IModelChangedEvent) -> Unit): IDisposable
    fun onDidChangeModelDecorations(listener: (e: IModelDecorationsChangedEvent) -> Unit): IDisposable
    fun onDidFocusEditorText(listener: () -> Unit): IDisposable
    fun onDidBlurEditorText(listener: () -> Unit): IDisposable
    fun onDidFocusEditorWidget(listener: () -> Unit): IDisposable
    fun onDidBlurEditorWidget(listener: () -> Unit): IDisposable
    fun onDidCompositionStart(listener: () -> Unit): IDisposable
    fun onDidCompositionEnd(listener: () -> Unit): IDisposable
    fun onDidAttemptReadOnlyEdit(listener: () -> Unit): IDisposable
    fun onDidPaste(listener: (e: IPasteEvent) -> Unit): IDisposable
    fun onMouseUp(listener: (e: IEditorMouseEvent) -> Unit): IDisposable
    fun onMouseDown(listener: (e: IEditorMouseEvent) -> Unit): IDisposable
    fun onContextMenu(listener: (e: IEditorMouseEvent) -> Unit): IDisposable
    fun onMouseMove(listener: (e: IEditorMouseEvent) -> Unit): IDisposable
    fun onMouseLeave(listener: (e: IPartialEditorMouseEvent) -> Unit): IDisposable
    fun onKeyUp(listener: (e: IKeyboardEvent) -> Unit): IDisposable
    fun onKeyDown(listener: (e: IKeyboardEvent) -> Unit): IDisposable
    fun onDidLayoutChange(listener: (e: EditorLayoutInfo) -> Unit): IDisposable
    fun onDidContentSizeChange(listener: (e: IContentSizeChangedEvent) -> Unit): IDisposable
    fun onDidScrollChange(listener: (e: IScrollEvent) -> Unit): IDisposable
    override fun saveViewState(): ICodeEditorViewState?
    override fun restoreViewState(state: ICodeEditorViewState)
    fun hasWidgetFocus(): Boolean
    fun <T : IEditorContribution> getContribution(id: String): T
    override fun getModel(): ITextModel?
    override fun setModel(model: ITextModel?)
    fun getOptions(): IComputedEditorOptions
    fun <T : EditorOption> getOption(id: T): FindComputedEditorOptionValueById<T>
    fun getRawOptions(): IEditorOptions
    fun getValue(options: `T$7` = definedExternally): String
    fun setValue(newValue: String)
    fun getContentWidth(): Number
    fun getScrollWidth(): Number
    fun getScrollLeft(): Number
    fun getContentHeight(): Number
    fun getScrollHeight(): Number
    fun getScrollTop(): Number
    fun setScrollLeft(newScrollLeft: Number, scrollType: ScrollType = definedExternally)
    fun setScrollTop(newScrollTop: Number, scrollType: ScrollType = definedExternally)
    fun setScrollPosition(position: INewScrollPosition, scrollType: ScrollType = definedExternally)
    fun getAction(id: String): IEditorAction
    fun executeCommand(source: String?, command: ICommand)
    fun pushUndoStop(): Boolean
    fun executeEdits(source: String?, edits: Array<IIdentifiedSingleEditOperation>, endCursorState: ICursorStateComputer = definedExternally): Boolean
    fun executeEdits(source: String?, edits: Array<IIdentifiedSingleEditOperation>, endCursorState: Array<Selection> = definedExternally): Boolean
    fun executeCommands(source: String?, commands: Array<ICommand?>)
    fun getLineDecorations(lineNumber: Number): Array<IModelDecoration>?
    fun deltaDecorations(oldDecorations: Array<String>, newDecorations: Array<IModelDeltaDecoration>): Array<String>
    fun getLayoutInfo(): EditorLayoutInfo
    fun getVisibleRanges(): Array<Range>
    fun getTopForLineNumber(lineNumber: Number): Number
    fun getTopForPosition(lineNumber: Number, column: Number): Number
    fun getContainerDomNode(): HTMLElement
    fun getDomNode(): HTMLElement?
    fun addContentWidget(widget: IContentWidget)
    fun layoutContentWidget(widget: IContentWidget)
    fun removeContentWidget(widget: IContentWidget)
    fun addOverlayWidget(widget: IOverlayWidget)
    fun layoutOverlayWidget(widget: IOverlayWidget)
    fun removeOverlayWidget(widget: IOverlayWidget)
    fun changeViewZones(callback: (accessor: IViewZoneChangeAccessor) -> Unit)
    fun getOffsetForColumn(lineNumber: Number, column: Number): Number
    fun render(forceRedraw: Boolean = definedExternally)
    fun getTargetAtClientPoint(clientX: Number, clientY: Number): IMouseTarget?
    fun getScrolledVisiblePosition(position: IPosition): `T$8`?
    fun applyFontInfo(target: HTMLElement)
}

external interface IDiffLineInformation {
    var equivalentLineNumber: Number
}

external interface IDiffEditor : IEditor {
    fun getDomNode(): HTMLElement
    fun onDidUpdateDiff(listener: () -> Unit): IDisposable
    override fun saveViewState(): IDiffEditorViewState?
    override fun restoreViewState(state: IDiffEditorViewState)
    override fun getModel(): IDiffEditorModel?
    override fun setModel(model: IDiffEditorModel?)
    fun getOriginalEditor(): ICodeEditor
    fun getModifiedEditor(): ICodeEditor
    fun getLineChanges(): Array<ILineChange>?
    fun getDiffLineInformationForOriginal(lineNumber: Number): IDiffLineInformation?
    fun getDiffLineInformationForModified(lineNumber: Number): IDiffLineInformation?
    fun updateOptions(newOptions: IDiffEditorOptions)
    override fun updateOptions(newOptions: IEditorOptions)
}

external open class FontInfo : BareFontInfo {
    open var _editorStylingBrand: Unit
    open var isTrusted: Boolean
    open var isMonospace: Boolean
    open var typicalHalfwidthCharacterWidth: Number
    open var typicalFullwidthCharacterWidth: Number
    open var canUseHalfwidthRightwardsArrow: Boolean
    open var spaceWidth: Number
    open var middotWidth: Number
    open var wsmiddotWidth: Number
    open var maxDigitWidth: Number
}

external open class BareFontInfo {
    open var _bareFontInfoBrand: Unit
    open var zoomLevel: Number
    open var fontFamily: String
    open var fontWeight: String
    open var fontSize: Number
    open var fontFeatureSettings: String
    open var lineHeight: Number
    open var letterSpacing: Number
}