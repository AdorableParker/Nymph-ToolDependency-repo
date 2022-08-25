plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
    `maven-publish`
}

group = "org.AdorableParker"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation(kotlin("reflect"))
}

publishing {
    publications {
        register<MavenPublication>("sqliteJDBC") {
            from(components["java"])
        }
    }
}