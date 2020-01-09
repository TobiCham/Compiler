package com.tobi.mc.computable

import com.tobi.mc.Data
import com.tobi.mc.FlowInterrupt

class ReturnResult(val result: Data) : FlowInterrupt() {

    override val description: String = "return ${result.description}"
}