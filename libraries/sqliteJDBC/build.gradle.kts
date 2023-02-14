plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
    `maven-publish`
    id("org.jetbrains.dokka") version "1.7.10"
}

group = "org.AdorableParker"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation(kotlin("reflect"))
}

publishing {
    publications {
        register<MavenPublication>("sqliteJDBC") {
            from(components["java"])
        }
    }
}