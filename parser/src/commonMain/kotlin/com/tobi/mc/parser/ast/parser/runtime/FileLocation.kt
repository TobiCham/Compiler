package com.tobi.mc.parser.ast.parser.runtime

data class FileLocation(val line: Int, val column: Int, val fileName: String? = null)