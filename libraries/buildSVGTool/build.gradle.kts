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
    implementation("com.beust:klaxon:5.5")
    implementation("org.jsoup:jsoup:1.14.3")

    implementation("org.apache.xmlgraphics:batik-all:1.14")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")

    testImplementation(kotlin("test"))
}

publishing {
    publications {
        register<MavenPublication>("BuildSVGTool") {
            from(components["java"])
        }
    }
}