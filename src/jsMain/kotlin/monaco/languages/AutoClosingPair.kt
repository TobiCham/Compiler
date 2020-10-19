package monaco.languages

data class AutoClosingPair(
    override var open: String,
    override var close: String
) : IAutoClosingPairConditional