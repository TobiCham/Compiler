kotlin {
    jvm {
        withJava()
        for(target in arrayOf("main", "test")) {
            compilations[target].compileKotlinTask.kotlinOptions.jvmTarget = "1.8"
        }
    }
    js {
        browser {
//            webpackTask {
//                cssSupport.enabled = true
//            }
//            runTask {
//                cssSupport.enabled = true
//            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":mips"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {}
        val jsMain by getting {
            dependencies {
                implementation(npm("monaco-editor", "0.21.2"))
                implementation(npm("monaco-editor-webpack-plugin", "2.0.0"))
                implementation(devNpm("style-loader", "2.0.0"))
                implementation(devNpm("css-loader", "5.0.0"))
                implementation(devNpm("file-loader", "6.1.1"))
                implementation(devNpm("webpack-dev-server", "3.10.3"))
            }
        }

        all {
            languageSettings.progressiveMode = true
        }
    }
}