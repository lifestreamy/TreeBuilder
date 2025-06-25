plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.33.0"
    signing
}

group = "com.github.lifestreamy.treebuilder"


val defaultUnknownVersion = "0.0.0-SNAPSHOT"

// Simple semantic versioning pattern: digits(any number of).digits.digits (optionally with "-SNAPSHOT" at the end)
fun String?.isValidVersion(): Boolean = this?.matches(Regex("""^v?(\d+\.\d+\.\d+)(-SNAPSHOT)?$""")) == true

val releaseVersion: String? by project
version = if (releaseVersion.isValidVersion()) releaseVersion!!.removePrefix("v") else {
    println("Warning: Invalid or missing version '$releaseVersion', falling back to $defaultUnknownVersion")
    defaultUnknownVersion
}

kotlin {
    jvmToolchain(17)
}


tasks.test {
    useJUnitPlatform()
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("dokka").get().asFile)
}

tasks.dokkaJavadoc.configure {
    outputDirectory.set(layout.buildDirectory.dir("dokkaJavadoc").get().asFile)
}

val dokkaJar by tasks.register<Jar>("dokkaJar") {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.get().outputDirectory)
}

val sourcesJar by tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}


mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "treebuilder", version.toString())

    pom {
        name.set("TreeBuilder")
        description.set("Small Kotlin library for building tree structures that can be traversed, mainly for menus")
        inceptionYear.set("2023")
        url.set("https://github.com/lifestreamy/TreeBuilder")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://opensource.org/licenses/Apache-2.0")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("lifestreamy")
                name.set("Tim K.")
                url.set("https://github.com/lifestreamy/")
            }
        }
        scm {
            url.set("https://github.com/lifestreamy/TreeBuilder")
            connection.set("scm:git:git://github.com/lifestreamy/TreeBuilder.git")
            developerConnection.set("scm:git:ssh://git@github.com/lifestreamy/TreeBuilder.git")
        }
    }
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}