import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.apache.xmlgraphics:batik-codec:1.14")
    implementation("org.apache.xmlgraphics:batik-svggen:1.14")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
//    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        register<MavenPublication>("BuildSVGTool") {
            from(components["java"])
        }
    }
}