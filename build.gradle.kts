plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.dokka") version "2.0.0"
//    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.33.0"
}


kotlin {
    group = "com.github"
    version = "2.0.1"

    jvmToolchain(17)

}

//publishing {
//    repositories {
//        maven {
//
//        }
//    }
//}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}