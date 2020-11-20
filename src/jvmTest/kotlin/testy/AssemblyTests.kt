package testy

import org.junit.jupiter.api.Test
import java.io.File

class AssemblyTests {

    @Test
    fun testAssembly() {
        val files = File(javaClass.classLoader.getResource("asm")!!.file)
        println(files)
    }
}