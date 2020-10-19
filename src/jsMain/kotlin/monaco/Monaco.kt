@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

package monaco

import org.w3c.dom.HTMLElement
import org.w3c.dom.Worker
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent

typealias Thenable<T> = PromiseLike<T>

external interface Environment {
    var baseUrl: String?
        get() = definedExternally
        set(value) = definedExternally
    val getWorker: ((workerId: String, label: String) -> Worker)?
        get() = definedExternally
    val getWorkerUrl: ((workerId: String, label: String) -> String)?
        get() = definedExternally
}

external interface IDisposable {
    fun dispose()
}

external interface IEvent<T> {
    @nativeInvoke
    operator fun invoke(listener: (e: T) -> Any, thisArg: Any = definedExternally): IDisposable
}

external open class Emitter<T> {
    open var event: IEvent<T>
    open fun fire(event: T)
    open fun dispose()
}

external enum class MarkerTag {
    Unnecessary /* = 1 */,
    Deprecated /* = 2 */
}

object MarkerSeverity {
    const val Hint = 1
    const val Info = 2
    const val Warning = 4
    const val Error = 8
}

external open class CancellationTokenSource(parent: CancellationToken = definedExternally) {
    open fun cancel()
    open fun dispose(cancel: Boolean = definedExternally)
}

external interface CancellationToken {
    var isCancellationRequested: Boolean
    var onCancellationRequested: (listener: (e: Any) -> Any, thisArgs: Any, disposables: Array<IDisposable>) -> IDisposable
}

external interface `T$0` {
    var scheme: String?
        get() = definedExternally
        set(value) = definedExternally
    var authority: String?
        get() = definedExternally
        set(value) = definedExternally
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var query: String?
        get() = definedExternally
        set(value) = definedExternally
    var fragment: String?
        get() = definedExternally
        set(value) = definedExternally
}

external interface `T$1` {
    var scheme: String
    var authority: String?
        get() = definedExternally
        set(value) = definedExternally
    var path: String?
        get() = definedExternally
        set(value) = definedExternally
    var query: String?
        get() = definedExternally
        set(value) = definedExternally
    var fragment: String?
        get() = definedExternally
        set(value) = definedExternally
}

external open class Uri : UriComponents {
    override var scheme: String
    override var authority: String
    override var path: String
    override var query: String
    override var fragment: String
    open fun with(change: `T$0`): Uri
    open fun toString(skipEncoding: Boolean = definedExternally): String
    open fun toJSON(): UriComponents

    companion object {
        fun isUri(thing: Any): Boolean
        fun parse(value: String, _strict: Boolean = definedExternally): Uri
        fun file(path: String): Uri
        fun from(components: `T$1`): Uri
        fun joinPath(uri: Uri, vararg pathFragment: String): Uri
        fun revive(data: UriComponents): Uri
        fun revive(data: Uri): Uri
        fun revive(data: UriComponents?): Uri?
        fun revive(data: Uri?): Uri?
        fun revive(data: UriComponents?): Uri?
        fun revive(data: Uri?): Uri?
        fun revive(data: UriComponents?): Uri?
        fun revive(data: Uri?): Uri?
    }
}

external interface UriComponents {
    var scheme: String
    var authority: String
    var path: String
    var query: String
    var fragment: String
}

object KeyCode {
    const val Unknown = 0
    const val Backspace = 1
    const val Tab = 2
    const val Enter = 3
    const val Shift = 4
    const val Ctrl = 5
    const val Alt = 6
    const val PauseBreak = 7
    const val CapsLock = 8
    const val Escape = 9
    const val Space = 10
    const val PageUp = 11
    const val PageDown = 12
    const val End = 13
    const val Home = 14
    const val LeftArrow = 15
    const val UpArrow = 16
    const val RightArrow = 17
    const val DownArrow = 18
    const val Insert = 19
    const val Delete = 20
    const val KEY_0 = 21
    const val KEY_1 = 22
    const val KEY_2 = 23
    const val KEY_3 = 24
    const val KEY_4 = 25
    const val KEY_5 = 26
    const val KEY_6 = 27
    const val KEY_7 = 28
    const val KEY_8 = 29
    const val KEY_9 = 30
    const val KEY_A = 31
    const val KEY_B = 32
    const val KEY_C = 33
    const val KEY_D = 34
    const val KEY_E = 35
    const val KEY_F = 36
    const val KEY_G = 37
    const val KEY_H = 38
    const val KEY_I = 39
    const val KEY_J = 40
    const val KEY_K = 41
    const val KEY_L = 42
    const val KEY_M = 43
    const val KEY_N = 44
    const val KEY_O = 45
    const val KEY_P = 46
    const val KEY_Q = 47
    const val KEY_R = 48
    const val KEY_S = 49
    const val KEY_T = 50
    const val KEY_U = 51
    const val KEY_V = 52
    const val KEY_W = 53
    const val KEY_X = 54
    const val KEY_Y = 55
    const val KEY_Z = 56
    const val Meta = 57
    const val ContextMenu = 58
    const val F1 = 59
    const val F2 = 60
    const val F3 = 61
    const val F4 = 62
    const val F5 = 63
    const val F6 = 64
    const val F7 = 65
    const val F8 = 66
    const val F9 = 67
    const val F10 = 68
    const val F11 = 69
    const val F12 = 70
    const val F13 = 71
    const val F14 = 72
    const val F15 = 73
    const val F16 = 74
    const val F17 = 75
    const val F18 = 76
    const val F19 = 77
    const val NumLock = 78
    const val ScrollLock = 79
    /**
     * Used for miscellaneous characters; it can vary by keyboard.
     * For the US standard keyboard the ';:' key
     */
    const val US_SEMICOLON = 80
    /**
     * For any country/region the '+' key
     * For the US standard keyboard the '=+' key
     */
    const val US_EQUAL = 81
    /**
     * For any country/region the '' key
     * For the US standard keyboard the '<' key
     */
    const val US_COMMA = 82
    /**
     * For any country/region the '-' key
     * For the US standard keyboard the '-_' key
     */
    const val US_MINUS = 83
    /**
     * For any country/region the '.' key
     * For the US standard keyboard the '.>' key
     */
    const val US_DOT = 84
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the '/?' key
    */
    const val US_SLASH = 85
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the '`~' key
    */
    const val US_BACKTICK = 86
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the '[{' key
    */
    const val US_OPEN_SQUARE_BRACKET = 87
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the '\|' key
    */
    const val US_BACKSLASH = 88
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the ']}' key
    */
    const val US_CLOSE_SQUARE_BRACKET = 89
   /**
    * Used for miscellaneous characters; it can vary by keyboard.
    * For the US standard keyboard the ''"' key
    */
    const val US_QUOTE = 90
    /**
    * Used for miscellaneous characters; it can vary by keyboard.
    */
    const val OEM_8 = 91
    /**
    * Either the angle bracket key or the backslash key on the RT 102-key keyboard.
    */
    const val OEM_102 = 92
    const val NUMPAD_0 = 93
    const val NUMPAD_1 = 94
    const val NUMPAD_2 = 95
    const val NUMPAD_3 = 96
    const val NUMPAD_4 = 97
    const val NUMPAD_5 = 98
    const val NUMPAD_6 = 99
    const val NUMPAD_7 = 100
    const val NUMPAD_8 = 101
    const val NUMPAD_9 = 102
    const val NUMPAD_MULTIPLY = 103
    const val NUMPAD_ADD = 104
    const val NUMPAD_SEPARATOR = 105
    const val NUMPAD_SUBTRACT = 106
    const val NUMPAD_DECIMAL = 107
    const val NUMPAD_DIVIDE = 108
    /**
    * Cover all key codes when IME is processing input.
    */
    const val KEY_IN_COMPOSITION = 109
    const val ABNT_C1 = 110
    const val ABNT_C2 = 111
    /**
    * Placed last to cover the length of the enum.
    * Please do not depend on this value!
    */
    const val MAX_VALUE = 112
}

external open class KeyMod {
    var CtrlCmd: Int
    var Shift: Int
    var Alt: Int
    var WinCtrl: Int
    fun chord(firstPart: Int, secondPart: Int): Int
}

external interface `T$2` {
    @nativeGetter
    operator fun get(href: String): UriComponents?
    @nativeSetter
    operator fun set(href: String, value: UriComponents)
}

external interface IMarkdownString {
    var value: String
    var isTrusted: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var supportThemeIcons: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var uris: `T$2`?
        get() = definedExternally
        set(value) = definedExternally
}

external interface IKeyboardEvent {
    var _standardKeyboardEventBrand: Boolean
    var browserEvent: KeyboardEvent
    var target: HTMLElement
    var ctrlKey: Boolean
    var shiftKey: Boolean
    var altKey: Boolean
    var metaKey: Boolean
    var keyCode: Int
    var code: String
    fun equals(keybinding: Number): Boolean
    fun preventDefault()
    fun stopPropagation()
}

external interface IMouseEvent {
    var browserEvent: MouseEvent
    var leftButton: Boolean
    var middleButton: Boolean
    var rightButton: Boolean
    var buttons: Number
    var target: HTMLElement
    var detail: Number
    var posx: Number
    var posy: Number
    var ctrlKey: Boolean
    var shiftKey: Boolean
    var altKey: Boolean
    var metaKey: Boolean
    var timestamp: Number
    fun preventDefault()
    fun stopPropagation()
}

external interface IScrollEvent {
    var scrollTop: Number
    var scrollLeft: Number
    var scrollWidth: Number
    var scrollHeight: Number
    var scrollTopChanged: Boolean
    var scrollLeftChanged: Boolean
    var scrollWidthChanged: Boolean
    var scrollHeightChanged: Boolean
}

external interface IPosition {
    var lineNumber: Number
    var column: Number
}

external open class Position(lineNumber: Number, column: Number) {
    open var lineNumber: Number
    open var column: Number
    open fun with(newLineNumber: Number = definedExternally, newColumn: Number = definedExternally): Position
    open fun delta(deltaLineNumber: Number = definedExternally, deltaColumn: Number = definedExternally): Position
    open fun equals(other: IPosition): Boolean
    open fun isBefore(other: IPosition): Boolean
    open fun isBeforeOrEqual(other: IPosition): Boolean
    open fun clone(): Position
    override fun toString(): String

    companion object {
        fun equals(a: IPosition?, b: IPosition?): Boolean
        fun isBefore(a: IPosition, b: IPosition): Boolean
        fun isBeforeOrEqual(a: IPosition, b: IPosition): Boolean
        fun compare(a: IPosition, b: IPosition): Number
        fun lift(pos: IPosition): Position
        fun isIPosition(obj: Any): Boolean
    }
}

external interface IRange {
    var startLineNumber: Number
    var startColumn: Number
    var endLineNumber: Number
    var endColumn: Number
}

external open class Range(startLineNumber: Number, startColumn: Number, endLineNumber: Number, endColumn: Number) {
    open var startLineNumber: Number
    open var startColumn: Number
    open var endLineNumber: Number
    open var endColumn: Number
    open fun isEmpty(): Boolean
    open fun containsPosition(position: IPosition): Boolean
    open fun containsRange(range: IRange): Boolean
    open fun strictContainsRange(range: IRange): Boolean
    open fun plusRange(range: IRange): Range
    open fun intersectRanges(range: IRange): Range?
    open fun equalsRange(other: IRange?): Boolean
    open fun getEndPosition(): Position
    open fun getStartPosition(): Position
    override fun toString(): String
    open fun setEndPosition(endLineNumber: Number, endColumn: Number): Range
    open fun setStartPosition(startLineNumber: Number, startColumn: Number): Range
    open fun collapseToStart(): Range

    companion object {
        fun isEmpty(range: IRange): Boolean
        fun containsPosition(range: IRange, position: IPosition): Boolean
        fun containsRange(range: IRange, otherRange: IRange): Boolean
        fun strictContainsRange(range: IRange, otherRange: IRange): Boolean
        fun plusRange(a: IRange, b: IRange): Range
        fun intersectRanges(a: IRange, b: IRange): Range?
        fun equalsRange(a: IRange?, b: IRange?): Boolean
        fun getEndPosition(range: IRange): Position
        fun getStartPosition(range: IRange): Position
        fun collapseToStart(range: IRange): Range
        fun fromPositions(start: IPosition, end: IPosition = definedExternally): Range
        fun lift(range: Nothing?): Nothing?
        fun lift(range: IRange): Range
        fun isIRange(obj: Any): Boolean
        fun areIntersectingOrTouching(a: IRange, b: IRange): Boolean
        fun areIntersecting(a: IRange, b: IRange): Boolean
        fun compareRangesUsingStarts(a: IRange?, b: IRange?): Number
        fun compareRangesUsingEnds(a: IRange, b: IRange): Number
        fun spansMultipleLines(range: IRange): Boolean
    }
}

external interface ISelection {
    var selectionStartLineNumber: Number
    var selectionStartColumn: Number
    var positionLineNumber: Number
    var positionColumn: Number
}

external open class Selection(selectionStartLineNumber: Number, selectionStartColumn: Number, positionLineNumber: Number, positionColumn: Number) : Range {
    open var selectionStartLineNumber: Number
    open var selectionStartColumn: Number
    open var positionLineNumber: Number
    open var positionColumn: Number
    override fun toString(): String
    open fun equalsSelection(other: ISelection): Boolean
    open fun getDirection(): SelectionDirection
    override fun setEndPosition(endLineNumber: Number, endColumn: Number): Selection
    open fun getPosition(): Position
    override fun setStartPosition(startLineNumber: Number, startColumn: Number): Selection

    companion object {
        fun selectionsEqual(a: ISelection, b: ISelection): Boolean
        fun fromPositions(start: IPosition, end: IPosition = definedExternally): Selection
        fun liftSelection(sel: ISelection): Selection
        fun selectionsArrEqual(a: Array<ISelection>, b: Array<ISelection>): Boolean
        fun isISelection(obj: Any): Boolean
        fun createWithDirection(startLineNumber: Number, startColumn: Number, endLineNumber: Number, endColumn: Number, direction: SelectionDirection): Selection
    }
}

external enum class SelectionDirection {
    LTR /* = 0 */,
    RTL /* = 1 */
}

external open class Token(offset: Number, type: String, language: String) {
    open var _tokenBrand: Unit
    open var offset: Number
    open var type: String
    open var language: String
    override fun toString(): String
}