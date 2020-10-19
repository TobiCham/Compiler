package monaco.languages

data class MonarchLanguageBracket(
    override var open: String,
    override var close: String,
    override var token: String
) : IMonarchLanguageBracket