group = "com.tobi"
version = "1.0"

plugins {
    kotlin("multiplatform") version "1.4.10"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        val jvmJar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
            doFirst {
                manifest {
                    attributes["Main-Class"] = "com.tobi.mc.main.MainKt"
                }
                from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
            }
        }
    }
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.0-M1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("commons-cli:commons-cli:1.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.6.2")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("monaco-editor", "0.21.2"))
                implementation(npm("monaco-editor-webpack-plugin", "2.0.0"))
                implementation(devNpm("style-loader", "2.0.0"))
                implementation(devNpm("css-loader", "5.0.0"))
                implementation(devNpm("file-loader", "6.1.1"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.0-M1")
            }
        }

        all {
            languageSettings.progressiveMode = true
        }
    }
}

tasks.wrapper {
    gradleVersion = "6.6.1"
}