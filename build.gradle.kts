group = "com.tobi"
version = "1.0"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

tasks.wrapper {
    gradleVersion = "6.0.1"
}