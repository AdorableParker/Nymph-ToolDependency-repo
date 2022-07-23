import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `java-library`
    `maven-publish`
}

group = "org.example"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("com.github.AdorableParker:nekoencode:0.1.0")

//        implementation('org.apache.xmlgraphics:batik-all:1.14')
//        implementation("org.apache.xmlgraphics:batik-xml:1.14")
//        implementation("org.apache.xmlgraphics:batik-anim:1.14")
//        implementation("org.apache.xmlgraphics:batik-svggen:1.14")
//        implementation("org.apache.xmlgraphics:batik-svg-dom:1.14")
        implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
//    implementation("org.jsoup:jsoup:1.14.3")
//    implementation("com.beust:klaxon:5.5")
    testImplementation(kotlin("test"))
}



tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}