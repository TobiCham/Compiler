kotlin {
    jvm {
        withJava()
        for (target in arrayOf("main", "test")) {
            compilations[target].compileKotlinTask.kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {}
        val jsMain by getting {}

        all {
            languageSettings.progressiveMode = true
        }
    }
}