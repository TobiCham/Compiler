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
                api(project(":parser"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.6.2")
//                implementation(kotlin("test-common"))
//                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting { }
        val jsMain by getting {}

        all {
            languageSettings.progressiveMode = true
        }
    }
}