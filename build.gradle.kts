group = "com.tobi"
version = "1.0"

plugins {
    kotlin("multiplatform") version "1.4.10"
}

kotlin {
    sourceSets {
        kotlin.apply {
            jvm {
                withJava()
                for(target in arrayOf("main", "test")) {
                    compilations[target].compileKotlinTask.kotlinOptions.jvmTarget = "1.8"
                }
            }
            js {
                browser()
            }
        }
        val commonMain by getting {}
    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.multiplatform")
    }
}

tasks.wrapper {
    gradleVersion = "6.6.1"
}