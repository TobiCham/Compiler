package monaco.languages

import com.tobi.mc.main.jsObject
import kotlin.js.RegExp

class SimpleTokeniser {

    class Entry(val name: String, vararg val rules: Rule)
    data class Rule(val regex: RegExp, val action: String)

    companion object {

        fun from(vararg entries: Entry): Any = jsObject {
            for(entry in entries) {
                this[entry.name] = makeEntry(*entry.rules)
            }
        }

        private fun makeEntry(vararg rules: Rule): Array<Any> = rules.map { rule ->
            arrayOf(rule.regex, rule.action)
        }.toTypedArray()
    }
}