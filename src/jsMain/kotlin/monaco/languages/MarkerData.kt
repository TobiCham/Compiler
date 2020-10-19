package monaco.languages

import monaco.editor.IMarkerData

data class MarkerData(
    override var severity: Int,
    override var message: String,
    override var startLineNumber: Number,
    override var startColumn: Number,
    override var endLineNumber: Number,
    override var endColumn: Number
) : IMarkerData