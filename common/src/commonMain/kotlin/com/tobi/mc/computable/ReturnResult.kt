package com.tobi.mc.computable

class ReturnResult(val result: Data) : FlowInterrupt() {

    override val description: String = "return ${result.description}"
}