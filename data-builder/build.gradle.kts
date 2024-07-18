plugins {
    kotlin("jvm")
    application
}
group = "org.alter"
version = "0.0.1"
description = "Builds OSRS Data for Alter Servers"

repositories {
    mavenCentral()
}
application {
    mainClass.set("org.alter.MainKt")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}