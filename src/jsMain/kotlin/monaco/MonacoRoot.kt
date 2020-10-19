@file:JsModule("monaco-editor")
@file:JsNonModule
package monaco

import monaco.editor.MonacoEditor
import monaco.languages.MonacoLanguages

external interface Monaco {

    val editor: MonacoEditor

    val languages: MonacoLanguages

    val KeyMod: KeyMod
}

external val _monaco: Monaco