plugins {
    kotlin("multiplatform") version "1.3.70"
}

kotlin {
    jvm {
        withJava()
        for(target in arrayOf("main", "test")) {
            compilations[target].compileKotlinTask.kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(project(":parser"))
                implementation(project(":intermediate"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.3")
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
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.3")
            }
        }

        all {
            languageSettings.progressiveMode = true
        }
    }
}