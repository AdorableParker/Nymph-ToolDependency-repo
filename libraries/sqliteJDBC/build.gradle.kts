plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
    `maven-publish`
}

group = "org.AdorableParker"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
}

publishing {
    publications {
        register<MavenPublication>("sqliteJDBC") {
            from(components["java"])
        }
    }
}