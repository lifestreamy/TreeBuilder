plugins {
    kotlin("jvm") version "2.1.0"
    `java-library`
}

val jvmVer : String by project
val jvmVerInt
    get() = jvmVer.toInt()


kotlin {
    group = "com.github"
    version = "2.0.0"

    jvmToolchain(jvmVerInt)

}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}