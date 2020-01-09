package com.tobi.mc

import com.google.gson.Gson
import com.tobi.mc.parser.Node
import com.tobi.mc.parser.ParseException
import java.io.*
import kotlin.concurrent.thread

class ParserWrapper(val parserJar: File) {

    fun parse(file: File): Node {
        return FileInputStream(file).use(this::parse)
    }

    fun parse(program: String): Node {
        return parse(ByteArrayInputStream(program.toByteArray()))
    }

    fun parse(input: InputStream): Node {
        val process = ProcessBuilder().run {
            command("java", "-jar", parserJar.absolutePath)
            directory(parserJar.parentFile)
            start()
        }
        input.copyTo(process.outputStream)
        process.outputStream.close()

        val outputstream = ByteArrayOutputStream()
        val errorStream = ByteArrayOutputStream()

        val thread = thread(true) {
            process.errorStream.copyTo(errorStream)
        }
        process.inputStream.copyTo(outputstream)
        while(thread.isAlive) Thread.sleep(0L)

        val exitCode = process.waitFor()
        if(exitCode != 0) {
            throw ParseException(String(outputstream.toByteArray()))
        }
        val lines = BufferedReader(InputStreamReader(ByteArrayInputStream(outputstream.toByteArray()))).readLines()
        var outputLine = lines.find { it.startsWith("//RESULT IS") } ?: throw ParseException(String(outputstream.toByteArray()))
        outputLine = outputLine.substring("//RESULT IS ".length)

        return Gson().fromJson(outputLine, Node::class.java)
    }
}