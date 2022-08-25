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
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        register<MavenPublication>("seed") {
            from(components["java"])
        }
    }
}